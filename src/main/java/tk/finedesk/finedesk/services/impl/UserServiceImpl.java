package tk.finedesk.finedesk.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Pair;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.finedesk.finedesk.aws.configuration.model.SQSEmailMessageDto;
import tk.finedesk.finedesk.aws.services.AmazonSQSService;
import tk.finedesk.finedesk.dto.request.RequestRegistrationDTO;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.dto.response.ResponseProfileDto;
import tk.finedesk.finedesk.dto.response.ResponseUserDto;
import tk.finedesk.finedesk.dto.response.ResponseUserRegistrationDto;
import tk.finedesk.finedesk.entities.User;
import tk.finedesk.finedesk.entities.UserProfile;
import tk.finedesk.finedesk.entities.UserRole;
import tk.finedesk.finedesk.entities.UserVerificationToken;
import tk.finedesk.finedesk.enums.ResponseEnum;
import tk.finedesk.finedesk.enums.Role;
import tk.finedesk.finedesk.repositories.UserProfileRepository;
import tk.finedesk.finedesk.repositories.UserRepository;
import tk.finedesk.finedesk.repositories.UserRoleRepository;
import tk.finedesk.finedesk.security.jwt.JwtCreator;
import tk.finedesk.finedesk.services.UserService;
import tk.finedesk.finedesk.services.UserVerificationTokenService;
import tk.finedesk.finedesk.utils.PasswordValidator;

import javax.management.relation.RoleNotFoundException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final PasswordValidator passwordValidator;
    private final UserVerificationTokenService userVerificationTokenService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            log.warn("User not found in DB, Checking in Inmemory users list.");
////            UserDetails inMemoryUser = inMemoryUserDetailsManager.loadUserByUsername(username);
////            if (inMemoryUser == null) {
////                log.error("User {} not found in db", username);
////                throw new UsernameNotFoundException("User not found in database");
////            }
////            return new org.springframework.security.core.userdetails.User(inMemoryUser.getUsername(), inMemoryUser.getPassword(), inMemoryUser.getAuthorities());
//        }
        log.info("User found in database: {}", username);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getUserRoles().forEach(userRole -> authorities.add(new SimpleGrantedAuthority(userRole.getRole().toString())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

    }

    @Transactional(rollbackFor = RoleNotFoundException.class)
    @Override
    public ResponseBaseDto registerUser(RequestRegistrationDTO userDto) throws IllegalArgumentException, RoleNotFoundException {

        boolean isValid = isPasswordValid(userDto);

        if (!isValid) {
            return ResponseBaseDto.builder().message("Password is not valid").build();
        }
        ResponseUserRegistrationDto responseUserRegistrationDto = new ResponseUserRegistrationDto();

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User user = modelMapper.map(userDto, User.class);

        try {
            setDefaultRole(user);
        } catch (RoleNotFoundException e) {
            throw new RoleNotFoundException(e.getMessage());
        }

        UserVerificationToken userVerificationToken = userVerificationTokenService.generateVerificationToken(user.getUsername());

        user.setUserVerificationToken(userVerificationToken);

        User savedUser = userRepository.save(user);

        responseUserRegistrationDto.setUUID(savedUser.getUuid());

        log.info("New user with username : {} registered", userDto.getUsername());
        return ResponseBaseDto.builder()
                .message(ResponseEnum.RESPONSE_200_USER_REGISTRATION.getMessage())
                .body(responseUserRegistrationDto)
                .build();
    }

    @Override
    public ResponseBaseDto registerAdmin(RequestRegistrationDTO adminDto) throws IllegalArgumentException, RoleNotFoundException {
        boolean isValid = isPasswordValid(adminDto);

        if (!isValid) {
            return ResponseBaseDto.builder().message("Password is not valid").build();
        }
        ResponseUserRegistrationDto responseUserRegistrationDto = new ResponseUserRegistrationDto();

        adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));

        User admin = modelMapper.map(adminDto, User.class);

        try {
            setAdminRole(admin);
        } catch (RoleNotFoundException e) {
            throw new RoleNotFoundException(e.getMessage());
        }

        UserVerificationToken userVerificationToken = userVerificationTokenService.generateVerificationToken(admin.getUsername());

        admin.setUserVerificationToken(userVerificationToken);

        User savedUser = userRepository.save(admin);

        responseUserRegistrationDto.setUUID(savedUser.getUuid());

        log.info("New admin with username : {} registered", adminDto.getUsername());

        //TODO implement email sending service with SQS

        return ResponseBaseDto.builder()
                .message(ResponseEnum.RESPONSE_200_USER_REGISTRATION.getMessage())
                .body(responseUserRegistrationDto)
                .build();
    }

    private void setDefaultRole(User user) throws RoleNotFoundException {
        Optional<UserRole> userRole = userRoleRepository.findByRole(Role.USER);
        if (userRole.isPresent()) {
            user.setUserRoles(Set.of(userRole.get()));
        } else {
            throw new RoleNotFoundException("No such role");
        }
    }

    private void setAdminRole(User admin) throws RoleNotFoundException {
        Optional<UserRole> userRole = userRoleRepository.findByRole(Role.ADMIN);
        if (userRole.isPresent()) {
            admin.setUserRoles(Set.of(userRole.get()));
        } else {
            throw new RoleNotFoundException("No such role");
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public boolean isUserExists(RequestRegistrationDTO userDto) throws RoleNotFoundException {
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user == null) {
            return false;
        }
        Set<UserRole> userRoles = user.getUserRoles();
        Object[] objects = userRoles.toArray();
        if (userRoles == null) {
            return false;
        } else if (((UserRole) objects[0]).getRole().equals(Role.ADMIN)) {
            addUserRoleToExistingAdmin(user);
            log.info("Added user role");
            return true;
        }
        return true;
    }

    @Override
    public ResponseBaseDto checkUser(String username) throws IllegalArgumentException {

        JwtCreator jwtCreator = new JwtCreator();

        UserDetails userDetails = loadUserByUsername(username);
        List<SimpleGrantedAuthority> roles = userDetails.getAuthorities().stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
        List<String> rolesString = roles.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
        UserVerificationToken userRefreshToken = userVerificationTokenService.generateRefreshToken(username);
        String refreshTokenUuid = userRefreshToken.getUuid();
        User user = userRepository.findByUsername(username);

        Optional<UserProfile> optionalProfile = userProfileRepository.findByUsername(username);

        UserProfile userProfile = optionalProfile.get();

        String uuid = user.getUuid();

        Pair<String, String> userUuid = Pair.of("userUuid", uuid);

        Pair<String, String> refreshToken = Pair.of("refreshTokenUuid", refreshTokenUuid);

        Pair<String, List<String>> userRoles = Pair.of("userRoles", rolesString);

        ChronoUnit hours = ChronoUnit.HOURS;

        String accessToken = jwtCreator.createAccessToken(username, refreshToken, hours, userUuid, userRoles);

        ResponseProfileDto profileDto = ResponseProfileDto
                .builder()
                .id(userProfile.getUuid())
                .username(username)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePhotoUrl(userProfile.getProfilePhotoURL())
                .coverPhotoUrl(userProfile.getCoverPhotoURL())
                .build();

        return ResponseBaseDto.builder()
                .message(accessToken)
                .body(profileDto)
                .build();
    }


    @Override
    public ResponseBaseDto getUserById(String userId) {

        Optional<User> userOptional = userRepository.findByUuid(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<UserProfile> userProfile = userProfileRepository.findByUsername(user.getUsername());
            if (userProfile.isPresent()) {
                UserProfile profile = userProfile.get();

                Set<UserRole> userRoles = user.getUserRoles();
                Set<String> roleString = userRoles.stream().map(userRole -> userRole.getRole().toString()).collect(Collectors.toSet());

                return ResponseBaseDto.builder()
                        .body(ResponseUserDto.builder()
                                .userUuid(user.getUuid())
                                .username(user.getUsername())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .roles(roleString)
                                .profileId(profile.getId())
                                .profilePhotoUrl(profile.getProfilePhotoURL())
                                .coverPhotoUrl(profile.getCoverPhotoURL())
                                .build()).message("Here is the User").build();

            } else {
                return ResponseBaseDto.builder().message("User doesn't have profile").build();
            }
        }
        return ResponseBaseDto.builder().message("Couldn't find user by provided id").build();
    }

    @Override
    public void addAdminRoleToExistingUser(RequestRegistrationDTO adminDto) throws RoleNotFoundException {
        User user = userRepository.findByUsername(adminDto.getUsername());
        Optional<UserRole> userRole = userRoleRepository.findByRole(Role.ADMIN);
        if (userRole.isPresent()) {
            Set<UserRole> userRoles = user.getUserRoles();
            userRoles.add(userRole.get());
            userRepository.save(user);
        } else {
            throw new RoleNotFoundException("No such role");
        }
    }

    @Override
    public boolean isRegisteredAsUser(String username) {
        User byUsername = userRepository.findByUsername(username);
        Set<UserRole> userRoles = byUsername.getUserRoles();
        Object[] objects = userRoles.toArray();
        return ((UserRole) objects[0]).getRole().equals(Role.USER);
    }

    public void addUserRoleToExistingAdmin(User existingUser) throws RoleNotFoundException {
        User user = userRepository.findByUsername(existingUser.getUsername());
        Optional<UserRole> userRole = userRoleRepository.findByRole(Role.USER);
        if (userRole.isPresent()) {
            Set<UserRole> userRoles = user.getUserRoles();
            userRoles.add(userRole.get());
            userRepository.save(user);
        } else {
            throw new RoleNotFoundException("No such role");
        }
    }

    public boolean isPasswordValid(RequestRegistrationDTO userDto) throws RoleNotFoundException {
        if (!isUserExists(userDto)) {
            passwordValidator.isPasswordValid(userDto.getPassword());
            return true;
        }
        return false;
    }


}

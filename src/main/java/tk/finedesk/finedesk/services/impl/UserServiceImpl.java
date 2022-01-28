package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
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
import tk.finedesk.finedesk.dto.request.RequestRegistrationDTO;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.dto.response.ResponseLoginDto;
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
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final PasswordValidator passwordValidator;
    private final UserVerificationTokenService userVerificationTokenService;
    private JwtCreator jwtCreator;


    @Transactional(rollbackFor = RoleNotFoundException.class)
    @Override
    public ResponseBaseDto registerUser(RequestRegistrationDTO userDto) throws IllegalAccessException, RoleNotFoundException {

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

        //TODO implement email sending service with SQS

        return ResponseBaseDto.builder()
                .message(ResponseEnum.RESPONSE_200_USER_REGISTRATION.getMessage())
                .body(responseUserRegistrationDto)
                .build();
    }

    private void setDefaultRole(User user) throws RoleNotFoundException {

        Optional<UserRole> userRole = userRoleRepository.findByRole(Role.ROLE_USER);
        if (userRole.isPresent()) {
            user.setUserRoles(Set.of(userRole.get()));
        } else {
            throw new RoleNotFoundException("No such role");
        }

    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public boolean isUserExists(RequestRegistrationDTO userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user != null) {
            log.info("User with username : {} already exists", userDto.getUsername());
            return true;
        }
        return false;
    }

    @Override
    public ResponseBaseDto checkUser(String username) throws IllegalArgumentException {

        jwtCreator = new JwtCreator();

        UserDetails userDetails = loadUserByUsername(username);
        List<SimpleGrantedAuthority> roles = userDetails.getAuthorities().stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
        List<String> rolesString = roles.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
        UserVerificationToken userRefreshToken = userVerificationTokenService.generateRefreshToken(username);
        String refreshTokenUuid = userRefreshToken.getUuid();
        User byUsername = userRepository.findByUsername(username);

        String uuid = byUsername.getUuid();

        Pair<String, String> userUuid = Pair.of("userUuid", uuid);

        Pair<String, String> refreshToken = Pair.of("refreshTokenUuid", refreshTokenUuid);

        Pair<String, List<String>> userRoles = Pair.of("userRoles", rolesString);

        ChronoUnit minutes = ChronoUnit.MINUTES;

        String accessToken = jwtCreator.createAccessToken(username, refreshToken, minutes, userUuid, userRoles);

        ResponseLoginDto responseLoginDto = ResponseLoginDto.builder().accessToken(accessToken).build();

        return ResponseBaseDto.builder()
                .body(responseLoginDto)
                .message("Here is the access token")
                .build();
    }

    @Override
    public List<ResponseBaseDto> getAllUsers() {

        List<User> allUsers = userRepository.findAll();

        List<ResponseBaseDto> collect = allUsers.stream().map(
                each -> getUserById(each.getId())).collect(Collectors.toList());


        return collect;

//        Set<String> uuids = new LinkedHashSet<>();
//        allUsers.forEach(
//                each -> uuids.add(each.getUuid())
//        );


//        return ResponseBaseDto.builder()
//                .body(ResponseUsersDto.builder().userUuids(uuids).build())
//                .message("Here are all the Users")
//                .build();
    }

    @Override
    public ResponseBaseDto getUserById(Long userId) {

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<UserProfile> userProfile = userProfileRepository.findByUsername(user.getUsername());
            if (userProfile.isPresent()) {
                UserProfile profile = userProfile.get();

                Set<UserRole> userRoles = user.getUserRoles();
                Set<String> roleString = userRoles.stream().map(userRole -> userRole.getRole().toString()).collect(Collectors.toSet());

                return ResponseBaseDto.builder()
                        .body(ResponseUserDto.builder()
                                .id(userId)
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


    public boolean isPasswordValid(RequestRegistrationDTO userDto) {
        if (!isUserExists(userDto)) {
            passwordValidator.isPasswordValid(userDto.getPassword());
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.error("User {} not found in db", username);
            throw new UsernameNotFoundException("User not found in database");
        }
        log.info("User found in database: {}", username);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getUserRoles().forEach(userRole -> authorities.add(new SimpleGrantedAuthority(userRole.getRole().toString())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

    }


}

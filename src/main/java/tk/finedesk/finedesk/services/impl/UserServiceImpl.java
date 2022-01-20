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
import tk.finedesk.finedesk.entities.User;
import tk.finedesk.finedesk.entities.UserRole;
import tk.finedesk.finedesk.entities.UserVerificationToken;
import tk.finedesk.finedesk.enums.ResponseEnum;
import tk.finedesk.finedesk.enums.Role;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

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

        ResponseUserDto responseUserDto = new ResponseUserDto();

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

        responseUserDto.setUUID(savedUser.getUuid());

        log.info("New user with username : {} registered", userDto.getUsername());

        //TODO implement email sending service with SQS

        return ResponseBaseDto.builder()
                .message(ResponseEnum.RESPONSE_200_USER_REGISTRATION.getMessage())
                .body(responseUserDto)
                .build();
    }

    private void setDefaultRole(User user) throws RoleNotFoundException {

        Optional<UserRole> userRole = userRoleRepository.findByRole(Role.ROLE_USER);
        if (userRole.isPresent()) {
            user.setUserRoles(List.of(userRole.get()));
        } else {
            throw new RoleNotFoundException("No such role");
        }

    }

    @Override
    public User getUser(String username) {
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

        UserVerificationToken userRefreshToken = userVerificationTokenService.generateRefreshToken(username);

        User byUsername = userRepository.findByUsername(username);
        String uuid = byUsername.getUuid();

        Pair<String, String> userUuid = Pair.of("uuid", uuid);

        Pair<String, String> refreshToken = Pair.of("refreshToken", userRefreshToken.toString());

        ChronoUnit minutes = ChronoUnit.MINUTES;

        String accessToken = jwtCreator.createAccessToken(username, refreshToken, minutes, userUuid);

        ResponseLoginDto responseLoginDto = ResponseLoginDto.builder().accessToken(accessToken).build();

        return ResponseBaseDto.builder().body(responseLoginDto).message("Here is the access token").build();
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

package com.cpe.springboot.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cpe.springboot.common.tools.DTOMapper;
import com.cpe.springboot.user.model.AuthDTO;
import com.cpe.springboot.user.model.UserDTO;
import com.cpe.springboot.user.model.UserModel;

//ONLY FOR TEST NEED ALSO TO ALLOW CROOS ORIGIN ON WEB BROWSER SIDE
@CrossOrigin
@RestController
public class UserRestController {

	private final UserService userService;

	public UserRestController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users")
	private List<UserDTO> getAllUsers() {
		List<UserDTO> uDTOList = new ArrayList<UserDTO>();
		for (UserModel uM : userService.getAllUsers()) {
			uDTOList.add(DTOMapper.fromUserModelToUserDTO(uM));
		}
		return uDTOList;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/{id}")
	private UserDTO getUser(@PathVariable String id) {
		Optional<UserModel> ruser;
		ruser = userService.getUser(id);
		if (ruser.isPresent()) {
			return DTOMapper.fromUserModelToUserDTO(ruser.get());
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id:" + id + ", not found", null);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/user")
	public UserDTO addUser(@RequestBody UserDTO user) {
		return userService.addUser(user);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/user/{id}")
	public UserDTO updateUser(@RequestBody UserDTO user, @PathVariable String id) {
		user.setId(Integer.valueOf(id));
		return userService.updateUser(user);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/user/{id}")
	public void deleteUser(@PathVariable String id) {
		userService.deleteUser(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/auth")
	private Integer getAllCourses(@RequestBody AuthDTO authDto) {
		try {
			List<UserModel> uList = userService.getUserByLoginPwd(authDto.getUsername(), authDto.getPassword());
			if (!uList.isEmpty()) {
				return ResponseEntity.ok(uList.get(0).getId());
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication error");
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/{id}/debit")
	public ResponseEntity<Boolean> debitUser(@PathVariable String id) {
		Optional<UserModel> optionalUser = userService.getUser(id);

		if (optionalUser.isPresent()) {
			UserModel user = optionalUser.get();
			// Supposons que le modèle utilisateur ait un champ account (solde du compte)
			float currentAccount = user.getAccount();

			if (currentAccount >= 10) {
				user.setAccount(currentAccount - 10);
				userService.updateUser(DTOMapper.fromUserModelToUserDTO(user)); // Mise à jour de l'utilisateur
				return ResponseEntity.ok(true); // Opération réussie, on retourne true
			} else {
				return ResponseEntity.ok(false); // Solde insuffisant, on retourne false
			}
		} else {
			return ResponseEntity.ok(false); // Utilisateur non trouvé, on retourne false
		}
	}

}

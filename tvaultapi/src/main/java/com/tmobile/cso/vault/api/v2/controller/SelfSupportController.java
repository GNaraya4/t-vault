// =========================================================================
// Copyright 2019 T-Mobile, US
// 
// Licensed under the Apache License, Version 2.0 (the "License")
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// See the readme.txt file for additional language around disclaimer of warranties.
// =========================================================================

package com.tmobile.cso.vault.api.v2.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.tmobile.cso.vault.api.common.TVaultConstants;
import com.tmobile.cso.vault.api.exception.TVaultValidationException;
import com.tmobile.cso.vault.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tmobile.cso.vault.api.service.SelfSupportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@CrossOrigin
@Api(description = "Manage Safes/SDBs", position = 21)
public class SelfSupportController {

	@Value("${vault.auth.method}")
	private String vaultAuthMethod;

	@Autowired
	private SelfSupportService selfSupportService;

	/**
	 * Reads the contents of a folder recursively
	 * @param token
	 * @param path
	 * @return
	 */
	@GetMapping(value="/v2/ss/sdb/list",produces="application/json")
	@ApiOperation(value = "${SelfSupportController.getFolders.value}", notes = "${SelfSupportController.getFolders.notes}")
	public ResponseEntity<String> getFoldersRecursively(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestParam("path") String path, @RequestParam( name="limit",required=false) Integer limit, @RequestParam( name="offset",required=false) Integer offset) {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.getFoldersRecursively(userDetails,  path, limit, offset);
	}
	/**
	 * Gets the list of all available safe names
	 * @param token
	 * @param path
	 * @return
	 */
	@GetMapping(value="/v2/ss/sdb/names",produces="application/json")
	@ApiOperation(value = "${SelfSupportController.getFolders.value}", notes = "${SelfSupportController.getFolders.notes}", hidden=true)
	public ResponseEntity<String> getSafeNames(HttpServletRequest request, @RequestHeader(value="vault-token") String token) {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.getAllSafeNames(userDetails);
	}
	/**
	 * 
	 * @param token
	 * @param path
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.getSafeAsPowerUser.value}", notes = "${SelfSupportController.getSafeAsPowerUser.notes}")
	@GetMapping(value="/v2/ss/sdb",produces="application/json")
	public ResponseEntity<String> getSafe(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestParam("path") String path){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.getSafe(userDetails, path);
	}
	/**
	 * Adds user with a Safe as Power User
	 * @param token
	 * @param safeUser
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.addUserToSafe.value}", notes = "${SelfSupportController.addUserToSafe.notes}")
	@PostMapping(value="/v2/ss/sdb/user",consumes="application/json",produces="application/json")
	public ResponseEntity<String> addUsertoSafe(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody SafeUser safeUser){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.addUserToSafe(userDetails,  safeUser);
	}
	/**
	 * 
	 * @param token
	 * @param safeUser
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.removeUserFromSafe.value}", notes = "${SelfSupportController.removeUserFromSafe.notes}")
	@DeleteMapping(value="/v2/ss/sdb/user")
	public ResponseEntity<String> deleteUserFromSafe(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody SafeUser safeUser){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.removeUserFromSafe(userDetails, safeUser);
	}
	/**
	 * Gets information about SDB
	 * @param token
	 * @param path
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.getInfoAsPowerUser.value}", notes = "${SelfSupportController.getInfoAsPowerUser.notes}")
	@GetMapping(value="/v2/ss/sdb/folder/{path}",produces="application/json")
	public ResponseEntity<String> getInfo(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestParam("path") String path){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.getInfo(userDetails, path);
	}

	/**
	 * 
	 * @param request
	 * @param token
	 * @param path
	 * @return
	 */
	@GetMapping(value="/auth/tvault/isauthorized",produces="application/json")
	@ApiOperation(value = "${SelfSupportController.authorized.value}", notes = "${SelfSupportController.authorized.notes}", hidden=true)
	public ResponseEntity<String> isAuthorized(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestParam("path") String path){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.isAuthorized(userDetails, path);
	}

	/**
	 *
	 * @param token
	 * @param safe
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.updateSafe.value}", notes = "${SelfSupportController.updateSafe.notes}")
	@PutMapping(value="/v2/ss/sdb", consumes="application/json",produces="application/json")
	public ResponseEntity<String> updateSafe(HttpServletRequest request, @RequestHeader(value="vault-token" ) String token, @RequestBody Safe safe) {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.updateSafe(userDetails,  safe);
	}

	/**
	 * Deletes a SDB folder
	 * @param token
	 * @param path
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.deleteSafe.value}", notes = "${SelfSupportController.deleteSafe.notes}")
	@DeleteMapping(value="/v2/ss/sdb/delete",produces="application/json")
	public ResponseEntity<String> deleteFolder(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestParam("path") String path){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.deletefolder(userDetails,path);
	}

	/**
	 * Adds a group to a safe
	 * @param token
	 * @param safeGroup
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.addGroupToSafe.value}", notes = "${SelfSupportController.addGroupToSafe.notes}")
	@PostMapping(value="/v2/ss/sdb/group",consumes="application/json",produces="application/json")
	public ResponseEntity<String> addGrouptoSafe(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody SafeGroup safeGroup){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.addGroupToSafe(userDetails,  safeGroup);
	}
	/**
	 * Removes a group from safe
	 * @param token
	 * @param safeGroup
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.deleteGroupFromSafe.value}", notes = "${SelfSupportController.deleteGroupFromSafe.notes}")
	@DeleteMapping (value="/v2/ss/sdb/group",consumes="application/json",produces="application/json")
	public ResponseEntity<String> deleteGroupFromSafe(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody SafeGroup safeGroup){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.removeGroupFromSafe(userDetails, safeGroup);
	}

	/**
	 * Adds AWS role to a Safe
	 * @param token
	 * @param awsRole
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.addAWSRoleToSafe.value}", notes = "${SelfSupportController.addAWSRoleToSafe.notes}")
	@PostMapping (value="/v2/ss/sdb/role",consumes="application/json",produces="application/json")
	public ResponseEntity<String> addAwsRoleToSafe(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody AWSRole awsRole){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.addAwsRoleToSafe(userDetails,  awsRole);
	}

	/**
	 * Remove AWS role from Safe and delete the role
	 * @param token
	 * @param awsRole
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.deleteAWSRoleFromSafe.value}", notes = "${SelfSupportController.deleteAWSRoleFromSafe.notes}")
	@DeleteMapping (value="/v2/ss/sdb/role",consumes="application/json",produces="application/json")
	public ResponseEntity<String> deleteAwsRoleFromSafe(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody AWSRole awsRole){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.removeAWSRoleFromSafe(userDetails, awsRole, false);
	}

	/**
	 * Detach AWS role from safe
	 * @param token
	 * @param awsRole
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.deleteAWSPermissionFromSafe.value}", notes = "${SelfSupportController.deleteAWSPermissionFromSafe.notes}")
	@PutMapping (value="/v2/ss/sdb/role",consumes="application/json",produces="application/json")
	public ResponseEntity<String> detachAwsRoleFromSafe(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody AWSRole awsRole) {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.removeAWSRoleFromSafe(userDetails,awsRole, true);
	}

	/**
	 * Associate approle to Safe
	 * @param token
	 * @param jsonstr
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.associateApprole.value}", notes = "${SelfSupportController.associateApprole.notes}")
	@PostMapping(value="/v2/ss/sdb/approle",consumes="application/json",produces="application/json")
	public ResponseEntity<String>associateApproletoSDB(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody SafeAppRoleAccess safeAppRoleAccess) {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.associateApproletoSDB(userDetails, safeAppRoleAccess);
	}

	/**
	 * Delete approle from Safe
	 * @param token
	 * @param jsonstr
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.deleteApproleFromSafe.value}", notes = "${SelfSupportController.deleteApproleFromSafe.notes}")
	@DeleteMapping(value="/v2/ss/sdb/approle",consumes="application/json",produces="application/json")
	public ResponseEntity<String>deleteApproleFromSDB(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody SafeAppRoleAccess safeAppRoleAccess) {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.deleteApproleFromSDB(userDetails, safeAppRoleAccess);
	}
	/**
	 * Method to create an EC2 AWS  role
	 * @param token
	 * @param awsLoginRole
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.createRole.value}", notes = "${SelfSupportController.createRole.notes}", hidden=true)
	@PostMapping(value="/v2/ss/auth/aws/role",consumes="application/json",produces="application/json")
	public ResponseEntity<String> createRole(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody AWSLoginRole awsLoginRole, @RequestParam("path") String path) throws TVaultValidationException {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.createRole(userDetails,awsLoginRole, path);
	}
	/**
	 * Method to update an aws app role.
	 * @param token
	 * @param jsonStr
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.updateRole.value}", notes = "${SelfSupportController.updateRole.notes}", hidden=true)
	@PutMapping(value="/v2/ss/auth/aws/role",consumes="application/json",produces="application/json")
	public ResponseEntity<String> updateRole(HttpServletRequest request,@RequestHeader(value="vault-token") String token, @RequestBody AWSLoginRole awsLoginRole, @RequestParam("path") String path) throws TVaultValidationException {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.updateRole(userDetails,awsLoginRole, path);
	}

	/**
	 * Method to create aws iam role
	 * @param token
	 * @param awsiamRole
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.createIamRole.value}", notes = "${SelfSupportController.createIamRole.notes}", hidden=true)
	@PostMapping(value="/v2/ss/auth/aws/iam/role",produces="application/json")
	public ResponseEntity<String> createIAMRole(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody AWSIAMRole awsiamRole, @RequestParam("path") String path) throws TVaultValidationException{
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.createIAMRole(userDetails, awsiamRole, path);
	}

	/**
	 * method to update aws iam role
	 * @param token
	 * @param awsiamRole
	 * @return
	 * @throws TVaultValidationException
	 */
	@ApiOperation(value = "${SelfSupportController.updateIAMRole.value}", notes = "${SelfSupportController.updateIAMRole.notes}", hidden=true)
	@PutMapping(value="/v2/ss/auth/aws/iam/role",consumes="application/json",produces="application/json")
	public ResponseEntity<String> updateRole(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody AWSIAMRole awsiamRole, @RequestParam("path") String path) throws TVaultValidationException {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.updateIAMRole(userDetails,  awsiamRole, path);
	}

	/**
	 * Create AppRole
	 * @param request
	 * @param token
	 * @param appRole
	 * @return
	 */
	@ApiOperation(value = "${AppRoleControllerV3.createAppRole.value}", notes = "${AppRoleControllerV3.createAppRole.notes}")
	@PostMapping(value="/v2/ss/auth/approle/role", consumes="application/json", produces="application/json")
	public ResponseEntity<String> createAppRole(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @Valid @RequestBody AppRole appRole){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.createAppRole(appRole, userDetails);
	}

	/**
	 * Delete AppRole
	 * @param request
	 * @param token
	 * @param rolename
	 * @return
	 */
	@ApiOperation(value = "${AppRoleControllerV3.deleteAppRole.value}", notes = "${AppRoleControllerV3.deleteAppRole.notes}")
	@DeleteMapping(value="/v2/ss/auth/approle/role/{role_name}",produces="application/json")
	public ResponseEntity<String> deleteAppRole(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @PathVariable("role_name" ) String rolename){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		AppRole appRole = new AppRole();
		appRole.setRole_name(rolename);
		return selfSupportService.deleteAppRole( appRole, userDetails);
	}
	
	/**
	 * Delete Secret_Ids
	 * @param request
	 * @param token
	 * @param appRoleAccessorIds
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.deleteSecretIds.value}", notes = "${SelfSupportController.deleteSecretIds.notes}")
	@DeleteMapping(value="/v2/ss/approle/{role_name}/secret_id",produces="application/json")
	public ResponseEntity<String> deleteSecretIds(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody AppRoleAccessorIds appRoleAccessorIds){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.deleteSecretIds(token, appRoleAccessorIds, userDetails);
	}
	
	/**
	 * To get/generate AppRole role_id
	 * @param token
	 * @param rolename
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.readAppRoleRoleId.value}", notes = "${SelfSupportController.readAppRoleRoleId.notes}")
	@GetMapping(value="/v2/ss/approle/{role_name}/role_id",produces="application/json")
	public ResponseEntity<String> readAppRoleRoleId(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @PathVariable("role_name") String rolename){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.readAppRoleRoleId(token, rolename, userDetails);
	}
	
	/**
	 * To get/read AppRole secret_id
	 * @param token
	 * @param rolename
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.readAppRoleSecretId.value}", notes = "${SelfSupportController.readAppRoleSecretId.notes}")
	@GetMapping(value="/v2/ss/approle/{role_name}/secret_id",produces="application/json")
	public ResponseEntity<String> readAppRoleSecretId(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @PathVariable("role_name" ) String rolename){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.readAppRoleSecretId( rolename, userDetails);
	}
	
	/**
	 * To get/read AppRole details
	 * @param token
	 * @param rolename
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.readAppRoleDetails.value}", notes = "${SelfSupportController.readAppRoleDetails.notes}")
	@GetMapping(value="/v2/ss/approle/{role_name}",produces="application/json")
	public ResponseEntity<String> readAppRoleDetails(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @PathVariable("role_name" ) String rolename){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.readAppRoleDetails(token, rolename, userDetails);
	}
	
	/**
	 * To get/read Accessors of all SecretIds issued against an AppRole
	 * @param token
	 * @param rolename
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.readAccessorsOfSecretIds.value}", notes = "${SelfSupportController.readAccessorsOfSecretIds.notes}")
	@GetMapping(value="/v2/ss/approle/{role_name}/accessors",produces="application/json")
	public ResponseEntity<String> readSecretIdAccessors(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @PathVariable("role_name" ) String rolename){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.readSecretIdAccessors(token, rolename, userDetails);
	}

	/**
	 * Reads the safes with read/write permission
	 * @param request
	 * @param token
	 * @return
	 */
	@GetMapping(value="/v2/ss/sdb/safes",produces="application/json")
	@ApiOperation(value = "${SelfSupportController.getsafes.value}", notes = "${SelfSupportController.getsafes.notes}")
	public ResponseEntity<String> getsafes(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestParam( name="limit",required=false) Integer limit, @RequestParam( name="offset",required=false) Integer offset) {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.getSafes(userDetails, limit, offset);
	}

	/**
	 * Reads approle names
	 * @param request
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.listAppRoles.value}", notes = "${SelfSupportController.listAppRoles.notes}")
	@GetMapping (value="/v2/ss/approle",produces="application/json")
	public ResponseEntity<String> listAppRoles(HttpServletRequest request, @RequestHeader(value="vault-token") String token,
																@RequestParam(name = "limit", required = false) Integer limit, @RequestParam(name = "offset", required = false) Integer offset){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.listAppRoles(token, userDetails, limit, offset);	
	}

	/**
	 * Reads approle names
	 * @param request
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "${AppRoleControllerV2.listAppRoles.value}", notes = "${AppRoleControllerV2.listAppRoles.notes}", hidden=true)
	@GetMapping (value="/v2/ss/approle/role",produces="application/json")
	public ResponseEntity<String> getAppRoles(HttpServletRequest request, @RequestHeader(value="vault-token") String token){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.readAppRoles( userDetails);
	}

	/**
	 * Retrieves the list of entities that are associated to this AppRole.
	 * Entities include safes, services accounts, and certs.
	 * @param token
	 * @param roleName - The name of the AppRole to get the list of associations for
	 * @return ResponseEntity
	 */
	@ApiOperation(value = "${SelfSupportController.listAppRoleEntityAssociations.value}", notes = "${SelfSupportController.listAppRoleEntityAssociations.notes}")
	@GetMapping(value="/v2/ss/approle/list/associations/{role_name}", produces="application/json")
	public ResponseEntity<String> listAppRoleEntityAssociations(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @PathVariable("role_name") String roleName){
		UserDetails userDetails = (UserDetails) request.getAttribute(TVaultConstants.USER_DETAILS);
		return selfSupportService.listAppRoleEntityAssociations(roleName, userDetails);
	}

	/**
	 * READ APPROLE
	 * @param token
	 * @param rolename
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.readAppRole.value}", notes = "${SelfSupportController.readAppRole.notes}", hidden=true)
	@GetMapping(value="/v2/ss/approle/role/{role_name}",produces="application/json")
	public ResponseEntity<String> readAppRole(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @PathVariable("role_name" ) String rolename){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.readAppRole( rolename, userDetails);
	}
	
	/**
	 * Create AppRole
	 * @param request
	 * @param token
	 * @param appRoleUpdate
	 * @return ResponseEntity
	 */
	@ApiOperation(value = "${SelfSupportController.updateAppRole.value}", notes = "${SelfSupportController.updateAppRole.notes}")
	@PutMapping(value="/v2/ss/approle", consumes="application/json", produces="application/json")
	public ResponseEntity<String> updateAppRole(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @Valid @RequestBody AppRoleUpdate appRoleUpdate){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.updateAppRole(token, appRoleUpdate, userDetails);
	}

	/**
	 * Returns the owner of the AppRole
	 * @param request
	 * @param token
	 * @param roleName
	 * @return ResponseEntity
	 */
	@ApiOperation(value = "${SelfSupportController.getAppRoleOwner.value}", notes = "${SelfSupportController.getAppRoleOwner.notes}")
	@GetMapping(value="/v2/ss/approle/{role_name}/owner", produces="application/json")
	public ResponseEntity<String[]> getApproleOwner(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @PathVariable("role_name") String roleName){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return ResponseEntity.status(HttpStatus.OK).body(selfSupportService.getAppRoleOwner(token, userDetails, roleName));
	}

	/**
	 * To transfer safe ownership.
	 * @param request
	 * @param token
	 * @param safeTransferRequest
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.transferSafe.value}", notes = "${SelfSupportController.transferSafe.notes}")
	@PostMapping(value="/v2/ss/transfersafe", consumes="application/json", produces="application/json")
	public ResponseEntity<String> transferSafe(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @Valid @RequestBody SafeTransferRequest safeTransferRequest){
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.transferSafe(token, safeTransferRequest, userDetails);
	}

	/**
	 * To list aws ec2 roles
	 * @param request
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.listRoles.value}", notes = "${SelfSupportController.listRoles.notes}")
	@GetMapping(value="/v2/ss/roles",produces="application/json")
	public ResponseEntity<String> listRoles(HttpServletRequest request,@RequestHeader(value="vault-token") String token){
		UserDetails userDetails = (UserDetails) (request).getAttribute("UserDetails");
		return selfSupportService.listRoles(token,userDetails);
	}
	/**
	 * Method to create an AWS EC2  role in new UI
	 * @param token
	 * @param awsLoginRole
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.createAwsec2Role.value}", notes = "${SelfSupportController.createAwsec2Role.notes}")
	@PostMapping(value="/v2/ss/auth/aws/awsec2/role",consumes="application/json",produces="application/json")
	public ResponseEntity<String> createAwsec2Role(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody AWSLoginRole awsLoginRole) throws TVaultValidationException {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.createAwsec2Role(userDetails, awsLoginRole);
	}
	/**
	 * Method to create AWS IAM role in new UI
	 * @param token
	 * @param awsiamRole
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.createAwsiamRole.value}", notes = "${SelfSupportController.createAwsiamRole.notes}")
	@PostMapping(value="/v2/ss/auth/aws/awsiam/role",produces="application/json")
	public ResponseEntity<String> createAwsiamRole(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody AWSIAMRole awsiamRole) throws TVaultValidationException{
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.createAwsiamRole(userDetails, awsiamRole);
	}
	/**
	 * Method to update an AWS EC2 role.
	 * @param token
	 * @param jsonStr
	 * @return
	 */
	@ApiOperation(value = "${SelfSupportController.updateAwsec2Role.value}", notes = "${SelfSupportController.updateAwsec2Role.notes}")
	@PutMapping(value="/v2/ss/auth/awsec2/role",consumes="application/json",produces="application/json")
	public ResponseEntity<String> updateAwsEc2Role(HttpServletRequest request,@RequestHeader(value="vault-token") String token, @RequestBody AWSLoginRole awsLoginRole) throws TVaultValidationException {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.updateAwsEc2Role(userDetails, token, awsLoginRole);
	}
	/**
	 * method to update AWS IAM role
	 * @param token
	 * @param awsiamRole
	 * @return
	 * @throws TVaultValidationException
	 */
	@ApiOperation(value = "${SelfSupportController.updateAwsiamRole.value}", notes = "${SelfSupportController.updateAwsiamRole.notes}")
	@PutMapping(value="/v2/ss/auth/awsiam/role",consumes="application/json",produces="application/json")
	public ResponseEntity<String> updateAwsIamRole(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestBody AWSIAMRole awsiamRole) throws TVaultValidationException {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.updateAwsIamRole(userDetails, token, awsiamRole);
	}

	/**
	 * Get list of safes with read/write/deny/owner permission.
	 * @param request
	 * @param token
	 * @return
	 */
	@GetMapping(value="/v2/ss/sdb/allsafes",produces="application/json")
	@ApiOperation(value = "${SelfSupportController.getAllSafes.value}", notes = "${SelfSupportController.getAllSafes.notes}")
	public ResponseEntity<String> getAllSafes(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestParam( name="search",required=false) String searchText) {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return selfSupportService.getAllSafes(userDetails, token, searchText);
	}
}

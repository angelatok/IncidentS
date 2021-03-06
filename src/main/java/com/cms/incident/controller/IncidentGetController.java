package com.cms.incident.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.incident.audit.ResourceNotFoundException;
import com.cms.incident.auth.AuthUtil;
import com.cms.incident.mqtt.GcMqttClient;
import com.cms.incident.repos.IIncidentModel;
import com.cms.incident.repos.IncidentModel;
import com.cms.incident.repos.IncidentService;

@RestController
@RequestMapping("/incident")
public class IncidentGetController {
	private IncidentService service;
	private GcMqttClient gcMqttClient;

	
	@Autowired
	public IncidentGetController(IncidentService incidentService, GcMqttClient gcMqttClient) {
		this.service = incidentService;
		this.gcMqttClient = gcMqttClient;
	}
	
	
	/**
	 * This API return all Incident from the database
	 * 
	 * @return
	 */
	@RolesAllowed("admin")
	@GetMapping("/all")
	public List<IncidentModel> getAll(HttpServletRequest request){
		AuthUtil.getInfo();
		List<IncidentModel> incidentList = service.getAll();
		
		gcMqttClient.publish(1, false, "incident", incidentList.toString());

		return incidentList;
	}
	
	/**
	 * 	This API return a summary list of incident base on the workspace id provided
	 * @param workspaceId
	 * @return
	 */
	@RolesAllowed("user")
	@GetMapping("/wsid/{wsid}")
	public List<IncidentModel> getAll(@PathVariable("wsid") String workspaceId){
		
		
		return service.getAllByWs(workspaceId);
	}
	
	/**
	 * This API return a summary list of incident base on the workspace id provided
	 * @param workspaceId
	 * @return
	 */
	@RolesAllowed("user")
	@GetMapping("/shortList/{wsid}")
	public List<IIncidentModel> getSummeryAll(@PathVariable("wsid") String workspaceId){
		return service.getIncidentSummery(workspaceId);
	}
	/**
	 * This API return a list of incident base on the request size and page from db.
	 * 
	 * @param page
	 * @param size
	 * @param id
	 * @return
	 */
	@RolesAllowed("user")
	@GetMapping("/page")
	public List<IIncidentModel> getSummeryPage(
			@RequestParam(value="pgnum", required = false) Integer page,
			@RequestParam(value="pgsize", required = false) Integer size,
			@RequestParam(value="wsid") String id){
		return service.getIncidentSummeryPg(id, page, size);
	}
	
	
	
	@RolesAllowed("user")
	@GetMapping("/{name}")
	public ResponseEntity<IncidentModel> getName(@PathVariable("name") String name){
		
		IncidentModel model = service.getByName(name).orElseThrow(() ->
		new ResourceNotFoundException("Not found with name = " + name));
		
		gcMqttClient.publish(1, false, "incident", model.toString());

	    return new ResponseEntity<>(model, HttpStatus.OK);

	    
	    
// Solution 2	this does not return a json error    
//		if(service.existsByName(name)){
//			Optional<IncidentShort> result = service.getByName(name);
//			return ResponseEntity.accepted().body(result.get());
//		}
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();		
	}
	
	
	
	@GetMapping("/name/{name}")
	public IncidentModel getMyName(@PathVariable("name") String name){
		
			Optional<IncidentModel> result = service.getByName(name);
			IncidentModel model = result.get();
			
			gcMqttClient.publish(1, false, "incident", model.toString());

			return model;
	}
	
	@RolesAllowed({"admin","user"})
	@GetMapping("/security")
	public List<IncidentModel> getAllSecurityType(){
		return service.getAllSecurityType();
	}
	
	// Note when did not specified @RolesAllowed, can access with no TOKEN 
	@GetMapping("/safety")
	public List<IncidentModel> getAllSafetyType(){
		return service.getAllSafetyType();
	}
	
	@GetMapping("/medical")
	public List<IncidentModel> getAllMedicalType(){
		return service.getAllMedicalTyoe();
	}
	@GetMapping("/public")
	public List<IncidentModel> getAllPublicType(){
		return service.getAllPublicType();
	}
	@GetMapping("/traffic")
	public List<IncidentModel> getAllTrafficType(){
		return service.getAllTrafficType();
	}
	@GetMapping("/others")
	public List<IncidentModel> getAllOthersype(){
		return service.getAllOthersType();
	}
	@GetMapping("/open")
	public List<IncidentModel> getAllOpenStatus(){
		return service.getAllOpenStatus();
	}
	@GetMapping("/pending")
	public List<IncidentModel> getAllPendingStatus(){
		return service.getAllPendingStatus();
	}
	@GetMapping("/closed")
	public List<IncidentModel> getAllCloseStatus(){
		return service.getdAllClosedStatus();
	}
	@GetMapping("/rejected")
	public List<IncidentModel> getAllRejectedStatus(){
		return service.getAllRejectedStatus();
	}	

}

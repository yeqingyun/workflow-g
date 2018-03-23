package com.gionee.gniflow.web.util;

import java.util.ArrayList;
import java.util.List;

import com.gionee.auth.model.Organization;
import com.gionee.auth.model.User;
import com.gionee.gniflow.web.response.OrganizationResponse;


public class DevResponseFactory {
	
	private static final String OPEN = "open";
	private static final String CLOSED = "closed";
	
	public static List<OrganizationResponse> convertOrganization(List<Organization> organizations) {
		List<OrganizationResponse> ret = new ArrayList<OrganizationResponse>();
		for (Organization organization:organizations) {
			OrganizationResponse organizationResponse = new OrganizationResponse(organization);
			if (organization.isLeaf()) {
				organizationResponse.setState(CLOSED);
			}
			else {
				organizationResponse.setState(CLOSED);
			}
			ret.add(organizationResponse);
		}
		return ret;
	}
	
	public static List<OrganizationResponse> convertOrganization(List<Organization> organizations, List<User> users) {
		List<OrganizationResponse> ret = new ArrayList<OrganizationResponse>();
		for (Organization organization:organizations) {
			OrganizationResponse organizationResponse = new OrganizationResponse(organization);
			organizationResponse.setState(CLOSED);
			ret.add(organizationResponse);
		}
		for (User user : users) {
			OrganizationResponse organizationResponse = new OrganizationResponse(user);
			organizationResponse.setState(OPEN);
			ret.add(organizationResponse);
		}
		return ret;
	}
}

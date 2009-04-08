/*
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.bi.report.portlet.action;

import com.liferay.util.bridges.simplemvc.Action;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.SystemException;
import com.liferay.bi.report.model.ReportDefinition;
import com.liferay.bi.report.model.impl.ReportDefinitionImpl;
import com.liferay.bi.report.service.ReportDefinitionLocalServiceUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

/**
 * <a href="UpdateDefinition.java.html"><b><i>View Source</i></b></a>
 *
 * @author Michael C. Han
 */
public class UpdateDefinitionAction implements Action {
	public boolean processAction(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws PortletException {
		long definitionId = ParamUtil.getLong(actionRequest, "definitionId");
		if (definitionId == -1) {
			//this should NEVER be -1...
			// TBD Need to do some processing here...to add error message?
			return false;
		}
		String name = ParamUtil.getString(actionRequest, "name");
		// TBD Need to set some validation...for instance name, format cannot be nul...
		String description = ParamUtil.getString(actionRequest, "description");
		String datasourceName = ParamUtil.getString(actionRequest, "dataSourceName");
		String format = ParamUtil.getString(actionRequest, "format");

		try {
			ReportDefinition definition =
				ReportDefinitionLocalServiceUtil.getReportDefinition(definitionId);
			definition.setName(name);
			definition.setDescription(description);
			definition.setDataSourceName(datasourceName);
			definition.setReportFormat(format);

			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			definition.setModifiedBy(themeDisplay.getUserId());

			definition =
				ReportDefinitionLocalServiceUtil.updateReportDefinition(definition);
			actionRequest.setAttribute("currentDefinition", definition);
			return true;
		}
		catch (Exception e) {
			throw new PortletException(
				"Unable to update definition: " + name, e);
		}
	}
}

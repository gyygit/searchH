/**
 * 
 */
package com.sinovatech.search.ectable.view;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.BuilderUtils;
import org.extremecomponents.table.view.html.TableActions;

/**
 * @author think
 * 
 */
public class SinovaExTableActions extends TableActions
{
	public SinovaExTableActions(TableModel model)
	{
		super(model);
	}

	/**
	 * 跳转到用户输入的页
	 * 
	 * @return
	 */
	public String getInputTargetPageAction()
	{
		StringBuffer action = new StringBuffer("javascript:");
		action.append(getClearedExportTableIdParameters());
		action.append(getFormParameterExt("p", "this.value"));
		action.append(getOnInvokeAction());
		return action.toString();
	}

	public String getFormParameterExt(String name, String value)
	{

		StringBuffer result = new StringBuffer();

		String form = BuilderUtils.getForm(this.getTableModel());
		result.append("document.forms.").append(form).append(".");
		result.append(
				this.getTableModel().getTableHandler().prefixWithTableId())
				.append(name);
		result.append(".value=").append(value).append(";");

		return result.toString();

	}

}

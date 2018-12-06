package com.sinovatech.search.ectable.view;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.toolbar.ImageItem;
import org.extremecomponents.util.HtmlBuilder;

public class SinovaExTableImageItem extends ImageItem
{

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.extremecomponents.table.view.html.toolbar.ImageItem#enabled(org.
	 * extremecomponents.util.HtmlBuilder,
	 * org.extremecomponents.table.core.TableModel)
	 */
	public void enabled(HtmlBuilder html, TableModel model)
	{
		html.a("#nogo");
		html.onclick(getAction());
		html.close();
		
		boolean showTooltips = model.getTableHandler().getTable()
				.isShowTooltips();
		if (showTooltips)
			html.img().src(getImage()).style(getStyle()).title(getTooltip())
					.onmouseover(getOnmouseover()).onmouseout(getOnmouseout())
					.alt(getAlt()).xclose();
		else
		{
			html.img().src(getImage()).style(getStyle()).onmouseover(
					getOnmouseover()).onmouseout(getOnmouseout()).alt(getAlt())
					.xclose();
		}
		html.aEnd();
	}

}

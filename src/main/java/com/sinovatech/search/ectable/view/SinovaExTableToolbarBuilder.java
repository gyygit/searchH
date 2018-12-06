/**
 * 
 */
package com.sinovatech.search.ectable.view;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.BuilderUtils;
import org.extremecomponents.table.view.html.ToolbarBuilder;
import org.extremecomponents.table.view.html.toolbar.ImageItem;
import org.extremecomponents.table.view.html.toolbar.ToolbarItemUtils;
import org.extremecomponents.util.HtmlBuilder;

/**
 * @author think
 * 
 */
public class SinovaExTableToolbarBuilder extends ToolbarBuilder {

	public SinovaExTableToolbarBuilder(HtmlBuilder html, TableModel model) {
		super(html, model);
	}

	public void firstPageItemAsImage() {
		ImageItem item = new SinovaImageItem();
		item.setTooltip(this.getMessages().getMessage(
				"toolbar.tooltip.firstPage"));
		item.setDisabledImage(BuilderUtils.getImage(this.getTableModel(),
				"firstPageDisabled"));
		item.setImage(BuilderUtils.getImage(this.getTableModel(), "firstPage"));
		item.setAlt(this.getMessages().getMessage("toolbar.text.firstPage"));
		item.setStyle("border:0");
		ToolbarItemUtils.buildFirstPage(this.getHtmlBuilder(), this
				.getTableModel(), item);
	}

	public void prevPageItemAsImage() {
		ImageItem item = new SinovaImageItem();
		item.setTooltip(this.getMessages().getMessage(
				"toolbar.tooltip.prevPage"));
		item.setDisabledImage(BuilderUtils.getImage(this.getTableModel(),
				"prevPageDisabled"));
		item.setImage(BuilderUtils.getImage(this.getTableModel(), "prevPage"));
		item.setAlt(this.getMessages().getMessage("toolbar.text.prevPage"));
		item.setStyle("border:0");
		ToolbarItemUtils.buildPrevPage(this.getHtmlBuilder(), this
				.getTableModel(), item);
	}

	public void nextPageItemAsImage() {
		ImageItem item = new SinovaImageItem();
		item.setTooltip(this.getMessages().getMessage(
				"toolbar.tooltip.nextPage"));
		item.setDisabledImage(BuilderUtils.getImage(this.getTableModel(),
				"nextPageDisabled"));
		item.setImage(BuilderUtils.getImage(this.getTableModel(), "nextPage"));
		item.setAlt(this.getMessages().getMessage("toolbar.text.nextPage"));
		item.setStyle("border:0");
		ToolbarItemUtils.buildNextPage(this.getHtmlBuilder(), this
				.getTableModel(), item);
	}

	public void lastPageItemAsImage() {
		ImageItem item = new SinovaImageItem();
		item.setTooltip(this.getMessages().getMessage(
				"toolbar.tooltip.lastPage"));
		item.setDisabledImage(BuilderUtils.getImage(this.getTableModel(),
				"lastPageDisabled"));
		item.setImage(BuilderUtils.getImage(this.getTableModel(), "lastPage"));
		item.setAlt(this.getMessages().getMessage("toolbar.text.lastPage"));
		item.setStyle("border:0");
		ToolbarItemUtils.buildLastPage(this.getHtmlBuilder(), this
				.getTableModel(), item);

	}

	public void rowsDisplayedDroplist() {
		int totalRows = this.getTableModel().getLimit().getTotalRows();
		int rowDisplay = this.getTableModel().getLimit()
				.getCurrentRowsDisplayed();

		int totalPages = (totalRows + rowDisplay - 1) / rowDisplay;

		this.getHtmlBuilder().append("每页显示");
		super.rowsDisplayedDroplist();
		this.getHtmlBuilder().append("条记录,&nbsp;跳转到第");
		this.getHtmlBuilder().input("text").size("3").value(
				String.valueOf(this.getTableModel().getLimit().getPage()));
		// 录入
		this.getHtmlBuilder().append(" onkeyup=").quote().append(
				"this.value=this.value.replace(/[^1-9]/g,''); if(this.value>")
				.append(totalPages).append("){this.value=").append(totalPages)
				.append(";}").quote();

		// 回车
		this.getHtmlBuilder().onkeypress(
				"if (event.keyCode == 13) {"
						+ new SinovaExTableActions(this.getTableModel())
								.getInputTargetPageAction() + "return false;}")
				.xclose();

		this.getHtmlBuilder().append("页");
	}
}

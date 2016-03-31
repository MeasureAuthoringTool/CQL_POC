package org.viewer.client;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

//import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
//import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
//import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HQMFViewer implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	private Map<String, TextArea> bookmarkMap = new LinkedHashMap<String, TextArea>();
	
	public void onModuleLoad() {

		TabLayoutPanel tabLayoutPanel = new TabLayoutPanel(2.5,  Unit.EM);
		final SplitLayoutPanel splitLayoutPanel = new SplitLayoutPanel();
		final HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanel.setSpacing(10);
		hPanel.add(new HTML("HQMF Viewer."));
		Button processHQMFButton = new Button("Process HQMF.");
		processHQMFButton.addStyleName("sendButton");
		hPanel.add(processHQMFButton);
		hPanel.add(new HTML("\t(For some reason, works only in IE right now.)"));
		
		final VerticalPanel summaryVerticalPanel = new VerticalPanel();
		summaryVerticalPanel.setSpacing(3);
		processHQMFButton.setStyleName("summaryVPanel");
		summaryVerticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		summaryVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		splitLayoutPanel.addNorth(hPanel, 50);
		splitLayoutPanel.addWest(summaryVerticalPanel, 0);

		final RichTextArea textArea = new RichTextArea();
		textArea.setTitle("Paste your HQMF XML here.");
		textArea.setText("Paste your HQMF XML here.");
		textArea.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(textArea.getText().length() < 30 && textArea.getText().equals("Paste your HQMF XML here.")){
					textArea.setText("");
				}
			}
		});
		textArea.addStyleName("boxsizingBorder");
		textArea.setWidth("100%");
		textArea.setHeight("100%");

		processHQMFButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				processHQMF(textArea, splitLayoutPanel, summaryVerticalPanel);
				splitLayoutPanel.remove(hPanel);
				splitLayoutPanel.setWidgetSize(summaryVerticalPanel, 200);
				splitLayoutPanel.setWidgetToggleDisplayAllowed(summaryVerticalPanel, true);
			}
		});
		splitLayoutPanel.add(textArea);
		
		MySplitLayoutPanel mySplitPanel = new MySplitLayoutPanel();
		final VerticalPanel testVerticalPanel = new VerticalPanel();
		testVerticalPanel.setSpacing(3);
		
		testVerticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		testVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		//final RichTextArea testtextArea = getSmartTextArea();
		//final AceEditor testtextArea = getAceEditor();
		AceEditor aceTxtArea = new AceEditor();
		aceTxtArea.setWidth("100%");
		aceTxtArea.setHeight("100%");
						
		mySplitPanel.addEast(testVerticalPanel, 0);
		mySplitPanel.add(aceTxtArea);
//		mySplitPanel.add(testtextArea);
		
		aceTxtArea.startEditor();
		
		setAceEditorProperties(aceTxtArea);		
		
		mySplitPanel.setWidgetToggleDisplayAllowed(testVerticalPanel, true);

		tabLayoutPanel.add(mySplitPanel, "test CQL Editor.");
		tabLayoutPanel.add(splitLayoutPanel, "HQMF Viewer");
				
		RootLayoutPanel.get().add(tabLayoutPanel);
	}

	public void setAceEditorProperties(AceEditor aceTxtArea) {
		aceTxtArea.setMode(AceEditorMode.CQL);
		aceTxtArea.setTheme(AceEditorTheme.ECLIPSE);
		aceTxtArea.getElement().getStyle().setFontSize(14, Unit.PX);
		aceTxtArea.setHighlightSelectedWord(true);
		aceTxtArea.setAutocompleteEnabled(true);
		aceTxtArea.setShowPrintMargin(false);		
	}

	public RichTextArea getSmartTextArea() {
		final RichTextArea testtextArea = new RichTextArea();
		testtextArea.setTitle("test test test.");
		testtextArea.setText("test test test.");
		testtextArea.addStyleName("boxsizingBorder");
		testtextArea.setWidth("100%");
		testtextArea.setHeight("100%");
		
//		testtextArea.addKeyPressHandler(new KeyPressHandler() {
//            @Override
//            public void onKeyPress(KeyPressEvent event) {
//                System.out.println( "KeyPressEvent:" +  
//                              event.getCharCode() + ":" +
//                              event.isAnyModifierKeyDown() + ":" + 
//                              event.isControlKeyDown());
//            }
//
//			
//        });
		
		testtextArea.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
            	
            	//if(event.getNativeKeyCode() == 32 && event.isControlKeyDown()){
            	if(event.getNativeKeyCode() == 32){
            		System.out.println("Show popup with context sensitive alternatives. Give focus to popup.");
            		event.preventDefault();
            		event.stopPropagation();
//            		int xCoOrdinate = event.getNativeEvent().getScreenX();
//            		int yCoOrdinate = event.getNativeEvent().getScreenY();
            		
            		RichTextArea richTextArea = (RichTextArea) event.getSource();
            		
            		String htmlText = richTextArea.getHTML();
            		boolean isEndParagraphTagRemoved = false;
            		System.out.println("before htmlText:"+htmlText);
            		if(htmlText.endsWith("</p>")){
            			htmlText = htmlText.substring(0, htmlText.length()-4);
            			isEndParagraphTagRemoved = true;
            		}
            		
            		System.out.println("after htmlText:"+htmlText);
            		
            		for(int i=htmlText.length()-2;i >= 0;i--){
            			char c =  htmlText.charAt(i);
            			
            			if(c == ' '){
            				String newText = htmlText.substring(i).trim();
            				if(newText.length() > 0){
            					htmlText = htmlText.substring(0, i);
            					String setHTMLString = htmlText + " <span style='color:blue'>"+newText+" </span>" + (isEndParagraphTagRemoved?"</p>":"");
            					System.out.println(setHTMLString);
            					richTextArea.setHTML(setHTMLString);
            				}else{
            					String setHTMLString = htmlText + (isEndParagraphTagRemoved?"</p>":"");
            					System.out.println(setHTMLString);
            					richTextArea.setHTML(setHTMLString);
            				}
            				
            				break;
            			}
            		}
            		System.out.println("New html:"+richTextArea.getHTML());
            		            		            		
//            		System.out.println("x:"+xCoOrdinate);
//            		System.out.println("y:"+yCoOrdinate);
//            		
//            		ListBox listBox = new ListBox();
//            		listBox.addItem("define", "define");
//            		listBox.addItem("function", "function");
//            		listBox.addItem("library", "library");
//            		listBox.addItem("using", "using");
//            		listBox.addItem("include", "include");
//            		listBox.addItem("public", "public");
//            		listBox.addItem("private", "private");
//            		listBox.addItem("valueset", "valueset");
//            		listBox.addItem("List", "List");
//            		listBox.addItem("Tuple", "Tuple");
//            		listBox.addItem("Interval", "Interval");
//            		
//            		listBox.setVisibleItemCount(listBox.getItemCount()+1);
            		
            		//PopupPanel popupPanel = new PopupPanel(true);
            		//popupPanel.add(listBox);
            		//popupPanel.setPopupPosition(xCoOrdinate, yCoOrdinate);
            		//popupPanel.show();
            		
            	}
            }
        });
		
		return testtextArea;
	}

	protected void handleTextAreaClick(FlowPanel flowPanel, DoubleClickEvent event, TextArea critReftextArea, ScrollPanel scrollPanel) {

		int textAreaCount = flowPanel.getWidgetCount();

		String idString = critReftextArea.getText();
		int idStart = idString.indexOf("<id");
		int idEnd = idString.indexOf("/>",idStart);
		idString = idString.substring(idStart, idEnd + "/>".length());

		String extension = idString.substring(idString.indexOf("extension"));
		extension = extension.substring(0,extension.indexOf("root"));
		extension = extension.trim();

		String root = idString.substring(idString.indexOf("root"));
		root = root.substring(0,root.indexOf("/>"));
		root = root.trim();

		for(int i=0;i<textAreaCount;i++){

			TextArea textArea = (TextArea)flowPanel.getWidget(i);
			if (textArea.getStyleName().indexOf("entry_background") == -1){
				continue;
			}

			String text = textArea.getText();
			if(text.indexOf(root) > -1 && text.indexOf(extension) > -1){
				DOM.scrollIntoView(textArea.getElement());
				break;
			}
		}
		System.out.println("Done.");
	}

	protected void processHQMF(RichTextArea textArea, SplitLayoutPanel splitLayoutPanel, VerticalPanel summaryVerticalPanel) {

		String text = textArea.getText();
		final ScrollPanel xmlPanel = getPanelForXML(text);
		if(xmlPanel != null){
			splitLayoutPanel.remove(textArea);
			splitLayoutPanel.add(xmlPanel);
		}
		
		for(final Map.Entry<String, TextArea> entry : bookmarkMap.entrySet()){
			String key = entry.getKey();
			Label label = new Label(key);
			label.setStyleName("bookMarkLabel");
			label.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					TextArea textArea = entry.getValue();
					xmlPanel.setVerticalScrollPosition(textArea.getElement().getOffsetTop());
				}
			});
			summaryVerticalPanel.add(label);
		}
	}

	private ScrollPanel getPanelForXML(String text) {

		String carriageReturn_LineFeed = "\r\n";

		final FlowPanel flowPanel = new FlowPanel();
		final ScrollPanel scrollPanel = new ScrollPanel();

		String lines[] = text.split("\\r?\\n");
		String stringBuilder = new String();
		for(int i=0;i<lines.length;i++){
			String line = lines[i];
			if(line.trim().startsWith("<criteriaReference")){

				TextArea textArea = new TextArea();
				textArea.setStyleName("textarea_noborder");
				textArea.setText(stringBuilder);
				flowPanel.add(textArea);
				textArea.setReadOnly(true);
				textArea.setWidth("100%");
				textArea.setHeight("100%");
				textArea.setVisibleLines(getLineCount(stringBuilder)+1);
				makeBookMarks(flowPanel, textArea);
								
				stringBuilder = new String();

				//find more lines until the end tag </criteriaReference>
				String critRefStringBuilder = new String();
				while(!line.trim().startsWith("</criteriaReference>")){
					critRefStringBuilder += (carriageReturn_LineFeed+line);
					i++;
					line = lines[i];
				}
				if(line.trim().startsWith("</criteriaReference>")){
					critRefStringBuilder += (carriageReturn_LineFeed+line);
					createCritRefTextArea(flowPanel, scrollPanel,
							critRefStringBuilder);
				}
			}else{
				String cleanLine = line.trim().replaceAll("<!--.*?-->", "");

				if(cleanLine.trim().startsWith("<entry")){

					if(stringBuilder.length() > 0){
						TextArea textArea = new TextArea();
						textArea.setStyleName("textarea_noborder");
											
						textArea.setEnabled(false);
						textArea.setWidth("100%");
						textArea.setHeight("100%");
						textArea.setText(stringBuilder);
						flowPanel.add(textArea);
						textArea.setVisibleLines(getLineCount(stringBuilder)+1);
						makeBookMarks(flowPanel, textArea);
					}
					
					stringBuilder = new String();

					//find more lines until the end tag </criteriaReference>
					String entryStringBuilder = new String();
					while(!line.trim().startsWith("</entry>")){
						entryStringBuilder += (carriageReturn_LineFeed+line);
						i++;
						line = lines[i];
					}
					if(line.trim().startsWith("</entry>")){
						entryStringBuilder += (carriageReturn_LineFeed+line);

						createEntryTextArea(flowPanel, entryStringBuilder, scrollPanel);
					}
				}else{
					stringBuilder += (carriageReturn_LineFeed+line);
				}
			}
		}
		if(stringBuilder.trim().length() > 0){
			TextArea textArea = new TextArea();
			textArea.setStyleName("textarea_noborder");
			textArea.setText(stringBuilder);
			flowPanel.add(textArea);
			textArea.setReadOnly(true);
			textArea.setWidth("100%");
			textArea.setHeight("100%");
			textArea.setVisibleLines(getLineCount(stringBuilder)+1);
			makeBookMarks(flowPanel, textArea);
		}
		
				
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("100%");
		vPanel.setHeight("100%");
		
		vPanel.add(flowPanel);
		scrollPanel.add(vPanel);
		return scrollPanel;
	}

	private int getLineCount(String stringBuilder) {		
		int lineCount = 0;
				
		String[] lines = stringBuilder.split("\\r?\\n");
		
		for(int i=0;i<lines.length;i++){
			String line = lines[i];
			if(line.length() > 150){
				lineCount += (int) Math.ceil(line.length()/150.0);
			}else{
				lineCount++;
			}
		}
		
		return lineCount;
	}

	public void createEntryTextArea(final FlowPanel flowPanel,
			String entryStringBuilder, ScrollPanel scrollPanel) {
		
		String carriageReturn_LineFeed = "\r\n";
		String lines[] = entryStringBuilder.split("\\r?\\n");
		String stringBuilder = new String();
		
		for(int i=0;i<lines.length;i++){
			String line = lines[i];
			if(line.trim().startsWith("<criteriaReference")){

				if(stringBuilder.length() > 0){
					TextArea entryTextArea = new TextArea();
					entryTextArea.setStyleName("textarea_noborder");
					entryTextArea.addStyleName("entry_background");
					entryTextArea.setText(stringBuilder);
					flowPanel.add(entryTextArea);
					entryTextArea.setReadOnly(true);
					entryTextArea.setWidth("100%");
					entryTextArea.setHeight("100%");
					entryTextArea.setVisibleLines(getLineCount(stringBuilder) + 1);
				}

				stringBuilder = new String();

				//find more lines until the end tag </criteriaReference>
				String critRefStringBuilder = new String();
				while(!line.trim().startsWith("</criteriaReference>")){
					critRefStringBuilder += (carriageReturn_LineFeed+line);
					i++;
					line = lines[i];
				}
				if(line.trim().startsWith("</criteriaReference>")){
					critRefStringBuilder += (carriageReturn_LineFeed+line);
					createCritRefTextArea(flowPanel, scrollPanel,
							critRefStringBuilder);
				}
			}else{
				stringBuilder += (carriageReturn_LineFeed+line);
			}
		}
		if(stringBuilder.length() > 0){
			TextArea entryTextArea = new TextArea();
			entryTextArea.setStyleName("textarea_noborder");
			entryTextArea.addStyleName("entry_background");
			entryTextArea.setText(stringBuilder);
			flowPanel.add(entryTextArea);
			entryTextArea.setReadOnly(true);
			entryTextArea.setWidth("100%");
			entryTextArea.setHeight("100%");
			entryTextArea.setVisibleLines(getLineCount(stringBuilder) + 1);
		}
		
	}

	public void createCritRefTextArea(final FlowPanel flowPanel,
			final ScrollPanel scrollPanel, String critRefStringBuilder) {
		final TextArea critReftextArea = new TextArea();
		critReftextArea.setStyleName("textarea_noborder");
		critReftextArea.addStyleName("criteriaRef_background");
		critReftextArea.setText(critRefStringBuilder);
		flowPanel.add(critReftextArea);
		critReftextArea.setReadOnly(true);
		critReftextArea.setWidth("100%");
		critReftextArea.setHeight("100%");
		critReftextArea.setVisibleLines(critRefStringBuilder.split("\\r?\\n").length + 1);
		critReftextArea.setTitle("Double click to go see the entry.");

		critReftextArea.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				handleTextAreaClick(flowPanel, event, critReftextArea, scrollPanel);							
			}

		});
	}
	
	private void makeBookMarks(FlowPanel flowPanel, TextArea textArea) {
		
		//TextArea firstTextArea = (TextArea) flowPanel.getWidget(0);
		
		//Check for data criteria section. This has Measure Details and the start of dataCriteriaSection.
		//We need to break it up so that "Measure Details" and dataCriteriaSection are 2 different textareas.

		String text = textArea.getText();
		int dataCritIndex = text.indexOf("<dataCriteriaSection>");
		if(dataCritIndex > -1){
			String measureDetailsText = text.substring(0,dataCritIndex);
			String dataCritSection = text.substring(dataCritIndex);
			textArea.setText(measureDetailsText);
			textArea.setVisibleLines(getLineCount(measureDetailsText) + 3);
			
			TextArea dataCritTextArea = new TextArea();
			dataCritTextArea.setStyleName("textarea_noborder");
			dataCritTextArea.setText(dataCritSection);
			flowPanel.add(dataCritTextArea);
			dataCritTextArea.setReadOnly(true);
			dataCritTextArea.setWidth("100%");
			dataCritTextArea.setHeight("100%");
			dataCritTextArea.setVisibleLines(getLineCount(dataCritSection) + 3);
			
			flowPanel.insert(dataCritTextArea, flowPanel.getWidgetIndex(textArea) + 1);
			bookmarkMap.put("Data Criteria Section", dataCritTextArea);
		}else{
			//make sure the textarea does not have a style class of 'entry_background' and 'criteriaRef_background',  
			//find '<populationCriteriaSection>'.
			int isEntryTextArea = textArea.getStyleName().indexOf("entry_background");
			int isCritRefTextArea = textArea.getStyleName().indexOf("criteriaRef_background");
			
			if (isEntryTextArea == -1 && isCritRefTextArea == -1){
				if(text.indexOf("<populationCriteriaSection>") > -1){
					int extensionIndex = text.indexOf("<id extension=\"");
					if(extensionIndex > -1){
						int startExtensionIndex = extensionIndex + "<id extension=\"".length();
						String extension = text.substring(startExtensionIndex, text.indexOf("\"",startExtensionIndex));
						System.out.println("Pop Crit Extension:"+extension);
						bookmarkMap.put(extension, textArea);
					}
				}
			}
		}
		
		//int textAreaCount = flowPanel.getWidgetCount();
		//Go through rest of the textAreas (from index 2) and for the textAreas which do  
		//not have a style class of 'entry_background' and 'criteriaRef_background',  
		//find '<populationCriteriaSection>'.
		
		//for(int i = 2;i < textAreaCount; i++){
			
//			TextArea textArea = (TextArea) flowPanel.getWidget(i);
//			int isEntryTextArea = textArea.getStyleName().indexOf("entry_background");
//			int isCritRefTextArea = textArea.getStyleName().indexOf("criteriaRef_background");
//			
//			if (isEntryTextArea == -1 && isCritRefTextArea == -1){
//				if(textArea.getText().indexOf("<populationCriteriaSection>") > -1){
//					String popCritText = textArea.getText();
//					int extensionIndex = popCritText.indexOf("<id extension=\"");
//					if(extensionIndex > -1){
//						int startExtensionIndex = extensionIndex + "<id extension=\"".length();
//						String extension = popCritText.substring(startExtensionIndex, popCritText.indexOf("\"",startExtensionIndex));
//						System.out.println("Pop Crit Extension:"+extension);
//						bookmarkMap.put(extension, textArea);
//					}
//				}
//			}
			
		//}
		
	}
}

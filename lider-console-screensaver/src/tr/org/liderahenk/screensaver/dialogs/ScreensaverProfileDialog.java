package tr.org.liderahenk.screensaver.dialogs;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.dialogs.IProfileDialog;
import tr.org.liderahenk.liderconsole.core.model.Profile;
import tr.org.liderahenk.screensaver.constants.ScreensaverConstants;
import tr.org.liderahenk.screensaver.i18n.Messages;
import tr.org.liderahenk.screensaver.utils.ScreensaverUtils;

public class ScreensaverProfileDialog implements IProfileDialog {
	
	private static final Logger logger = LoggerFactory.getLogger(ScreensaverProfileDialog.class);
	
	// Widgets
	private TabFolder tabFolder;
	
	private Combo cmbDisplay;
	
	private Label lblScreen;
	private Label lblBlankMinute;
	private Label lblChange;
	private Label lblCycleMinute;
	private Label lblLockMinute;
	
	private Spinner spnScreenTime;
	private Spinner spnChangeTime;
	private Spinner spnLockTime;
	private Spinner spnStandby;
	private Spinner spnSuspend;
	private Spinner spnOff;
	private Spinner spnFading;
	
	private Button btnCheckLock;
	private Button btnCheckGrabImage;
	private Button btnCheckGrabVideo;
	private Button btnCheckPowerManagement;
	private Button btnCheckPowerOff;
	private Button btnCheckFadeToBlack;
	private Button btnCheckFadeFromBlack;
	private Button btnCheckInstallColormap;
	private Button[] btnTextChoices;
	
	private Text txtText;
	private Text txtURL;
	
	// Combo values
	private final String[] modesArr = new String[] { "DISABLE", "BLANK", "ONLY_ONE", "RANDOM", "SAME_RANDOM" };
	
	
	@Override
	public void init() {
	}
	
	@Override
	public void createDialogArea(Composite parent, Profile profile) {
		logger.debug("Profile recieved: {}", profile != null ? profile.toString() : null);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		tabFolder = new TabFolder(composite, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		createDisplayModesTab(profile);
		createImageManipulationTab(profile);
		createPowerManagementTab(profile);
		createTextManipulationTab(profile);
		createFadingTab(profile);
	}
	
	public void createDisplayModesTab(Profile profile) {
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
	    tabItem.setText(Messages.getString("MODE"));
	    
	    Group group = new Group(tabFolder, SWT.NONE);
	    group.setLayout(new GridLayout(3, false));
	    group.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 3));
	    
	    Label lblDisplayMode = new Label(group, SWT.NONE);
	    lblDisplayMode.setText(Messages.getString("DISPLAYING_MODE"));
	    lblDisplayMode.pack();
	    
	    cmbDisplay = new Combo(group, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
	    cmbDisplay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
	    for (int i = 0; i < modesArr.length; i++) {
			String i18n = Messages.getString(modesArr[i]);
			if (i18n != null && !i18n.isEmpty()) {
				cmbDisplay.add(i18n);
				cmbDisplay.setData(i + "", modesArr[i]);
			}
		}
	    cmbDisplay.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	            if (cmbDisplay.getSelectionIndex() == 0) {
	            	
	            	lblScreen.setEnabled(false);
					spnScreenTime.setEnabled(false);
					lblBlankMinute.setEnabled(false);
					lblChange.setEnabled(false);
					spnChangeTime.setEnabled(false);
					lblCycleMinute.setEnabled(false);
					btnCheckLock.setSelection(false);
					btnCheckLock.setEnabled(false);
					spnLockTime.setEnabled(false);
					lblLockMinute.setEnabled(false);
	            }
	            else {
	            	
	            	lblScreen.setEnabled(true);
					spnScreenTime.setEnabled(true);
					lblBlankMinute.setEnabled(true);
					lblChange.setEnabled(true);
					spnChangeTime.setEnabled(true);
					lblCycleMinute.setEnabled(true);
					btnCheckLock.setEnabled(true);
					lblLockMinute.setEnabled(true);
	            }
	        }
	    });
	    selectOption(cmbDisplay, profile != null && profile.getProfileData() != null
				? profile.getProfileData().get(ScreensaverConstants.PARAMETERS.MODES) : null);
	    cmbDisplay.pack();
	    
	    lblScreen = new Label(group, SWT.NONE);
	    lblScreen.setText(Messages.getString("BLACK_SCREEN"));
	    lblScreen.pack();
	    
	    spnScreenTime = new Spinner(group, SWT.BORDER);
	    spnScreenTime.setMinimum(ScreensaverConstants.MIN_VALUE);
	    spnScreenTime.setIncrement(1);
	    spnScreenTime.setMaximum(ScreensaverConstants.MAX_VALUE);
	    spnScreenTime.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	    String minute = (profile != null && profile.getProfileData() != null
				? (String) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.BLACK_SCREEN) : "0");
	    spnScreenTime.setSelection(Integer.parseInt(minute));
	    spnScreenTime.pack();
	    
	    lblBlankMinute = new Label(group, SWT.NONE);
	    lblBlankMinute.setText(Messages.getString("MINUTE"));
	    lblBlankMinute.pack();
	    
	    lblChange = new Label(group, SWT.NONE);
	    lblChange.setText(Messages.getString("CHANGE"));
	    lblChange.pack();
	    
	    spnChangeTime = new Spinner(group, SWT.BORDER);
	    spnChangeTime.setMinimum(ScreensaverConstants.MIN_VALUE);
	    spnChangeTime.setIncrement(1);
	    spnChangeTime.setMaximum(ScreensaverConstants.MAX_VALUE);
	    spnChangeTime.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	    minute = (profile != null && profile.getProfileData() != null
				? (String) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.CHANGE) : "0");
	    spnChangeTime.setSelection(Integer.parseInt(minute));
	    spnChangeTime.pack();
	    
	    lblCycleMinute = new Label(group, SWT.NONE);
	    lblCycleMinute.setText(Messages.getString("MINUTE"));
	    lblCycleMinute.pack();
	    
	    btnCheckLock = new Button(group, SWT.CHECK);
	    btnCheckLock.setText(Messages.getString("LOCK"));
	    btnCheckLock.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spnLockTime.setEnabled(btnCheckLock.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	    boolean isSelected = (profile != null && profile.getProfileData() != null
				? (boolean) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.LOCK_SCREEN) : false);
	    btnCheckLock.setSelection(isSelected);
	    btnCheckLock.pack();
	    
	    spnLockTime = new Spinner(group, SWT.BORDER);
	    spnLockTime.setMinimum(ScreensaverConstants.MIN_VALUE);
	    spnLockTime.setIncrement(1);
	    spnLockTime.setMaximum(ScreensaverConstants.MAX_VALUE);
	    spnLockTime.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	    minute = (profile != null && profile.getProfileData() != null
				? (String) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.LOCK) : "0");
	    spnLockTime.setSelection(Integer.parseInt(minute));
	    spnLockTime.setEnabled(isSelected);
	    spnLockTime.pack();
	    
	    lblLockMinute = new Label(group, SWT.NONE);
	    lblLockMinute.setText(Messages.getString("MINUTE"));
	    lblLockMinute.pack();
	    
	    tabItem.setControl(group);
	}
	
	public void createImageManipulationTab(Profile profile) {
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
	    tabItem.setText(Messages.getString("MANIPULATION"));
	    
	    Group group = new Group(tabFolder, SWT.NONE);
	    group.setLayout(new GridLayout(1, false));
	    group.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
	    
	    btnCheckGrabImage = new Button(group, SWT.CHECK);
	    btnCheckGrabImage.setText(Messages.getString("GRAB_IMAGE"));
	    boolean isSelected = (profile != null && profile.getProfileData() != null
				? (boolean) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.GRAB_IMAGE) : false);
	    btnCheckGrabImage.setSelection(isSelected);
	    btnCheckGrabImage.pack();
	    
	    btnCheckGrabVideo = new Button(group, SWT.CHECK);
	    btnCheckGrabVideo.setText(Messages.getString("GRAB_VIDEO"));
	    isSelected = (profile != null && profile.getProfileData() != null
				? (boolean) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.GRAB_VIDEO) : false);
	    btnCheckGrabVideo.setSelection(isSelected);
	    btnCheckGrabVideo.pack();
	    
	    tabItem.setControl(group);
	}
	
	public void createPowerManagementTab(Profile profile) {
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
	    tabItem.setText(Messages.getString("POWER_MANAGEMENT"));
	    
	    Group group = new Group(tabFolder, SWT.NONE);
	    group.setLayout(new GridLayout(3, false));
	    group.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 5, 3));
	    
	    btnCheckPowerManagement = new Button(group, SWT.CHECK);
	    btnCheckPowerManagement.setText(Messages.getString("ENABLE_POWER_MANAGEMENT"));
	    btnCheckPowerManagement.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1));
	    boolean isSelected = (profile != null && profile.getProfileData() != null
				? (boolean) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.ENABLE_POWER_MNG) : false);
	    btnCheckPowerManagement.setSelection(isSelected);
	    btnCheckPowerManagement.pack();
	    
	    Label lblStandby = new Label(group, SWT.NONE);
	    lblStandby.setText(Messages.getString("STANDBY"));
	    lblStandby.pack();
	    
	    spnStandby = new Spinner(group, SWT.BORDER);
	    spnStandby.setMinimum(ScreensaverConstants.MIN_VALUE);
	    spnStandby.setIncrement(1);
	    spnStandby.setMaximum(ScreensaverConstants.MAX_VALUE);
	    spnStandby.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	    String minute = (profile != null && profile.getProfileData() != null
				? (String) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.STANDBY) : "0");
	    spnStandby.setSelection(Integer.parseInt(minute));
	    spnStandby.pack();
	    
	    Label lblStandbyMinute = new Label(group, SWT.NONE);
	    lblStandbyMinute.setText(Messages.getString("MINUTE"));
	    lblStandbyMinute.pack();
	    
	    Label lblSuspend = new Label(group, SWT.NONE);
	    lblSuspend.setText(Messages.getString("SUSPEND"));
	    lblSuspend.pack();
	    
	    spnSuspend = new Spinner(group, SWT.BORDER);
	    spnSuspend.setMinimum(ScreensaverConstants.MIN_VALUE);
	    spnSuspend.setIncrement(1);
	    spnSuspend.setMaximum(ScreensaverConstants.MAX_VALUE);
	    spnSuspend.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	    minute = (profile != null && profile.getProfileData() != null
				? (String) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.SUSPEND) : "0");
	    spnSuspend.setSelection(Integer.parseInt(minute));
	    spnSuspend.pack();
	    
	    Label lblSuspendMinute = new Label(group, SWT.NONE);
	    lblSuspendMinute.setText(Messages.getString("MINUTE"));
	    lblSuspendMinute.pack();
	    
	    Label lblOff = new Label(group, SWT.NONE);
	    lblOff.setText(Messages.getString("OFF"));
	    lblOff.pack();
	    
	    spnOff = new Spinner(group, SWT.BORDER);
	    spnOff.setMinimum(ScreensaverConstants.MIN_VALUE);
	    spnOff.setIncrement(1);
	    spnOff.setMaximum(ScreensaverConstants.MAX_VALUE);
	    spnOff.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	    minute = (profile != null && profile.getProfileData() != null
				? (String) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.OFF) : "0");
	    spnOff.setSelection(Integer.parseInt(minute));
	    spnOff.pack();
	    
	    Label lblOffMinute = new Label(group, SWT.NONE);
	    lblOffMinute.setText(Messages.getString("MINUTE"));
	    lblOffMinute.pack();
	    
	    btnCheckPowerOff = new Button(group, SWT.CHECK);
	    btnCheckPowerOff.setText(Messages.getString("ENABLE_QUICK_POWER_OFF"));
	    btnCheckPowerOff.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1));
	    isSelected = (profile != null && profile.getProfileData() != null
				? (boolean) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.ENABLE_QUICK_POWER_OFF) : false);
	    btnCheckPowerOff.setSelection(isSelected);
	    btnCheckPowerOff.pack();
	    
	    tabItem.setControl(group);
	}
	
	public void createTextManipulationTab(Profile profile) {
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
	    tabItem.setText(Messages.getString("TEXT_MANIPULATION"));
	    
	    Group group = new Group(tabFolder, SWT.NONE);
	    group.setLayout(new GridLayout(2, false));
	    group.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 3));
	    
	    btnTextChoices  = new Button[3];
	    
	    btnTextChoices[0] = new Button(group, SWT.RADIO);
	    btnTextChoices[0].setSelection(true);
	    btnTextChoices[0].setText(Messages.getString("HOST_NAME"));
	    btnTextChoices[0].setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
	    boolean isSelected = (profile != null && profile.getProfileData() != null
				? (boolean) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.HOST_NAME_TIME) : false);
	    btnTextChoices[0].setSelection(isSelected);
	    btnTextChoices[0].pack();

	    btnTextChoices[1] = new Button(group, SWT.RADIO);
	    btnTextChoices[1].setText(Messages.getString("TEXT"));
	    btnTextChoices[1].addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtText.setEnabled(btnTextChoices[1].getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	    isSelected = (profile != null && profile.getProfileData() != null
				? (boolean) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.TEXT) : false);
	    btnTextChoices[1].setSelection(isSelected);
	    btnTextChoices[1].pack();
	    
	    txtText = new Text(group, SWT.BORDER);
	    txtText.setEnabled(isSelected);
	    String txt = (profile != null && profile.getProfileData() != null
				? (String) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.TXT_FOR_TEXT) : "");
	    txtText.setText(txt);
	    txtText.pack();

	    btnTextChoices[2] = new Button(group, SWT.RADIO);
	    btnTextChoices[2].setText(Messages.getString("URL"));
	    btnTextChoices[2].addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtURL.setEnabled(btnTextChoices[2].getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	    isSelected = (profile != null && profile.getProfileData() != null
				? (boolean) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.URL) : false);
	    btnTextChoices[2].setSelection(isSelected);
	    btnTextChoices[2].pack();
	    
	    txtURL = new Text(group, SWT.BORDER);
	    txtURL.setEnabled(isSelected);
	    txt = (profile != null && profile.getProfileData() != null
				? (String) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.TXT_FOR_URL) : "");
	    txtURL.setText(txt);
	    txtURL.pack();
	    
	    tabItem.setControl(group);
	    
	}
	
	public void createFadingTab(Profile profile) {
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
	    tabItem.setText(Messages.getString("FADING"));
	    
	    Group group = new Group(tabFolder, SWT.NONE);
	    group.setLayout(new GridLayout(3, false));
	    group.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 4));
	    
	    btnCheckFadeToBlack = new Button(group, SWT.CHECK);
	    btnCheckFadeToBlack.setText(Messages.getString("FADE_TO_BLACK"));
	    btnCheckFadeToBlack.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1));
	    boolean isSelected = (profile != null && profile.getProfileData() != null
				? (boolean) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.FADE_TO_BLACK) : false);
	    btnCheckFadeToBlack.setSelection(isSelected);
	    btnCheckFadeToBlack.pack();
	    
	    btnCheckFadeFromBlack = new Button(group, SWT.CHECK);
	    btnCheckFadeFromBlack.setText(Messages.getString("FADE_FROM_BLACK"));
	    btnCheckFadeFromBlack.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1));
	    isSelected = (profile != null && profile.getProfileData() != null
				? (boolean) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.FADE_FROM_BLACK) : false);
	    btnCheckFadeFromBlack.setSelection(isSelected);
	    btnCheckFadeFromBlack.pack();
	    
	    Label lblTime = new Label(group, SWT.NONE);
	    lblTime.setText(Messages.getString("FADING_TIME"));
	    lblTime.pack();
	    
	    spnFading = new Spinner(group, SWT.BORDER);
	    spnFading.setMinimum(ScreensaverConstants.MIN_VALUE);
	    spnFading.setIncrement(1);
	    spnFading.setMaximum(ScreensaverConstants.FADING_MAX_VALUE);
	    spnFading.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	    String minute = (profile != null && profile.getProfileData() != null
				? (String) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.FADE_TIME) : "0");
	    spnFading.setSelection(Integer.parseInt(minute));
	    spnFading.pack();
	    
	    Label lblSeconds = new Label(group, SWT.NONE);
	    lblSeconds.setText(Messages.getString("SECONDS"));
	    lblSeconds.pack();
	    
	    btnCheckInstallColormap = new Button(group, SWT.CHECK);
	    btnCheckInstallColormap.setText(Messages.getString("INSTALL_COLORMAP"));
	    isSelected = (profile != null && profile.getProfileData() != null
				? (boolean) profile.getProfileData().get(ScreensaverConstants.PARAMETERS.INSTALL_COLORMAP) : false);
	    btnCheckInstallColormap.setSelection(isSelected);
	    btnCheckInstallColormap.pack();
	    
	    tabItem.setControl(group);
	}
	
	@Override
	public Map<String, Object> getProfileData() throws Exception {
		Map<String, Object> profileData = new HashMap<String, Object>();
		profileData.put(ScreensaverConstants.PARAMETERS.MODES, ScreensaverUtils.getSelectedValue(cmbDisplay));
		profileData.put(ScreensaverConstants.PARAMETERS.BLACK_SCREEN, spnScreenTime.getText());
		profileData.put(ScreensaverConstants.PARAMETERS.CHANGE, spnChangeTime.getText());
		profileData.put(ScreensaverConstants.PARAMETERS.LOCK_SCREEN, btnCheckLock.getSelection());
		profileData.put(ScreensaverConstants.PARAMETERS.LOCK, spnLockTime.getText());
		profileData.put(ScreensaverConstants.PARAMETERS.BLACK_SCREEN, spnScreenTime.getText());
		profileData.put(ScreensaverConstants.PARAMETERS.GRAB_IMAGE, btnCheckGrabImage.getSelection());
		profileData.put(ScreensaverConstants.PARAMETERS.GRAB_VIDEO, btnCheckGrabVideo.getSelection());
		profileData.put(ScreensaverConstants.PARAMETERS.ENABLE_POWER_MNG, btnCheckPowerManagement.getSelection());
		profileData.put(ScreensaverConstants.PARAMETERS.STANDBY, spnStandby.getText());
		profileData.put(ScreensaverConstants.PARAMETERS.SUSPEND, spnSuspend.getText());
		profileData.put(ScreensaverConstants.PARAMETERS.OFF, spnOff.getText());
		profileData.put(ScreensaverConstants.PARAMETERS.ENABLE_QUICK_POWER_OFF, btnCheckPowerOff.getSelection());
		profileData.put(ScreensaverConstants.PARAMETERS.HOST_NAME_TIME, btnTextChoices[0].getSelection());
		profileData.put(ScreensaverConstants.PARAMETERS.TEXT, btnTextChoices[1].getSelection());
		profileData.put(ScreensaverConstants.PARAMETERS.TXT_FOR_TEXT, txtText.getText());
		profileData.put(ScreensaverConstants.PARAMETERS.URL, btnTextChoices[2].getSelection());
		profileData.put(ScreensaverConstants.PARAMETERS.TXT_FOR_URL, txtURL.getText());
		profileData.put(ScreensaverConstants.PARAMETERS.FADE_TO_BLACK, btnCheckFadeToBlack.getSelection());
		profileData.put(ScreensaverConstants.PARAMETERS.FADE_FROM_BLACK, btnCheckFadeFromBlack.getSelection());
		profileData.put(ScreensaverConstants.PARAMETERS.FADE_TIME, spnFading.getText());
		profileData.put(ScreensaverConstants.PARAMETERS.INSTALL_COLORMAP, btnCheckInstallColormap.getSelection());
		
		return profileData;
	}
	
	private void selectOption(Combo combo, Object value) {
		if (value != null) {
			for (int i = 0; i < modesArr.length; i++) {
				if (modesArr[i].equalsIgnoreCase(value.toString())) {
					combo.select(i);
				}
			}
		}
	}
	
}

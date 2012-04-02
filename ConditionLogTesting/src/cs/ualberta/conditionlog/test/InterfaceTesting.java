package cs.ualberta.conditionlog.test;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;
import android.widget.Button;
import cs.ualberta.conditionlog.view.MainView;
import cs.ualberta.conditionlog.view.NewPhotoView;

public class InterfaceTesting extends ActivityInstrumentationTestCase2<MainView> {
	protected static final int INITIAL_POSITION = 0;
	/**
	 * @uml.property  name="thisActivity"
	 * @uml.associationEnd  
	 */ 
	private MainView thisActivity;
	private Button thisButton;
	private ActivityMonitor activityMonitor;
	
	public InterfaceTesting() {
		super("cs.ualberta.conditionlog", MainView.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		
		activityMonitor = getInstrumentation().addMonitor(NewPhotoView.class.getName(), null, false);
		
		thisActivity = getActivity();
	}
	
	public void testOpenCamera() {
		thisButton = (Button) thisActivity.findViewById(cs.ualberta.conditionlog.R.id.TakePhotoButton);
		assertTrue(thisButton.isClickable());
		thisActivity.runOnUiThread(new Runnable() {
			
			public void run() {
				thisButton.performClick();
			}
		});
		
		NewPhotoView nextActivity = (NewPhotoView) getInstrumentation().waitForMonitor(activityMonitor);
		assertNotNull("failure", nextActivity);
	}
	
	public void testGeneratePicture() {
		thisButton = (Button) thisActivity.findViewById(cs.ualberta.conditionlog.R.id.TakePhotoButton);
		thisActivity = this.getActivity();
		
		thisButton = (Button) thisActivity.findViewById(cs.ualberta.conditionlog.R.id.TakeAPhoto);
		assertTrue("nope", thisButton.isClickable());
	}
}

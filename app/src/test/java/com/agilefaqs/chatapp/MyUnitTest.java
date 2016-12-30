package com.agilefaqs.chatapp;

import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.confengine.chatapp.BuildConfig;
import com.confengine.chatapp.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**

 * Created by javed_a on 12/28/2016.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, sdk= 23 ,packageName = "com.agilefaqs.chatapp")
public class MyUnitTest {
//Creating reference for Main Activity Class
    private MainActivity mainActivity;
    private RecyclerView messagesList;
    private EditText messageInput;
    private Button sendButton;
    private List<String> messages;

    @Before
    public void setUp() throws Exception {
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        messagesList = (RecyclerView) mainActivity.findViewById(R.id.messagesList);
        messageInput = (EditText) mainActivity.findViewById(R.id.messageInput);
        sendButton = (Button)mainActivity.findViewById(R.id.sendButton);

    }

    //Validating Null Check
    @Test
    public void test_Init() throws Exception {
        assertNotNull(mainActivity);
        assertNotNull(messageInput);
        assertNotNull(sendButton);
    }

    //Validating Click operation
    @Test
    public void test_onClick() throws Exception {
        sendButton.performClick();
    }
}

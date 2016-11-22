package com.cbrain.cmh.selvbetjening;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.*;
import org.junit.runner.RunWith;

import java.util.Map;

import static android.support.test.espresso.Espresso.onData;
import static org.hamcrest.Matchers.*;

/**
 * Created by CMH on 21-11-2016.
 */
@RunWith(AndroidJUnit4.class)
public class Tester {
    @Rule
    public ActivityTestRule<Controller> mActivityRule
            = new ActivityTestRule<>(Controller.class);

    @Test
    public void testtitle() {
        new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {

            }
        };

    }

}

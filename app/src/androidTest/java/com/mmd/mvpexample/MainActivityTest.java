package com.mmd.mvpexample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;

import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private SimpleIdlingResource idlingResource;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        idlingResource = new SimpleIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void testSuccessfulLogin() {
        onView(withId(R.id.et_name)).perform(typeText("testUser"), closeSoftKeyboard());
        onView(withId(R.id.et_password)).perform(typeText("password123"), closeSoftKeyboard());

        onView(withId(R.id.progress_login))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        onView(withId(R.id.bt_submit)).perform(click());

        idlingResource.setIdleState(false);

        onView(withId(R.id.progress_login))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        idlingResource.setIdleState(true);

        onView(withId(R.id.progress_login))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void testLoginFailure() {
        onView(withId(R.id.et_name)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.et_password)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.bt_submit)).perform(click());

        // تاخیر برای اطمینان از نمایش Toast
        onView(isRoot()).perform(waitFor(3000)); // افزایش زمان تأخیر به ۳ ثانیه

        // استفاده از یک DecorViewMatcher مستقل بدون دسترسی مستقیم به activity
        onView(withText("Login Fail"))
                .inRoot(new ToastMatcher()) // استفاده از ToastMatcher برای شناسایی Toast
                .check(matches(isDisplayed()));
    }

    public class ToastMatcher extends TypeSafeMatcher<Root> {
        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                return windowToken == appToken;
            }
            return false;
        }
    }

    public static ViewAction waitFor(final long delay) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "انتظار برای " + delay + " میلی‌ثانیه";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }
}
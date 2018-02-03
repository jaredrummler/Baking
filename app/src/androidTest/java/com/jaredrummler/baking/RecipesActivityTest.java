package com.jaredrummler.baking;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jaredrummler.baking.ui.details.DetailsActivity;
import com.jaredrummler.baking.ui.recipes.RecipesActivity;
import com.jaredrummler.baking.ui.recipes.RecipesFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    @Rule
    public ActivityTestRule<RecipesActivity> activity = new ActivityTestRule<>(RecipesActivity.class);
    private final SimpleIdlingResource idlingResource = new SimpleIdlingResource();

    @Before
    public void setup() {
        IdlingRegistry.getInstance().register(idlingResource);
        idlingResource.setIdleState(false);
        RecipesFragment fragment = (RecipesFragment) activity.getActivity()
                .getSupportFragmentManager().findFragmentById(R.id.fragment_recipes);
        fragment.setListener(resource -> idlingResource.setIdleState(true));
    }

    @Test
    public void start_details_activity_on_click() {
        Intents.init();
        onView(withId(R.id.rv_recipes)).perform(actionOnItemAtPosition(0, click()));
        Intents.intended(hasComponent(DetailsActivity.class.getName()));
        Intents.release();
    }

    @After
    public void cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

}

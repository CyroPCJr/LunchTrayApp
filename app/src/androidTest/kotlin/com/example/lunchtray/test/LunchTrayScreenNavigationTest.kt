package com.example.lunchtray.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.lunchtray.LunchTrayApp
import com.example.lunchtray.LunchTrayScreen
import com.example.lunchtray.R
import com.example.lunchtray.datasource.DataSource
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LunchTrayScreenNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setupCupcakeNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            LunchTrayApp(navController = navController)
        }
    }

    @Test
    fun lunchTrayNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(expectedRouteName = LunchTrayScreen.Start.name)
    }

    @Test
    fun lunchTrayNavHost_verifyBackNavigationNotShownOnStartOrderScreen() {
        val backText =
            composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun lunchTrayNavHost_clickStartOrderScreen_navigatesToEntreeMenuScreen() {
        composeTestRule.onNodeWithStringId(R.string.start_order).performClick()
    }

    private fun performanceClickNextButton() {
        composeTestRule.onNodeWithText("NEXT").performClick()
    }

    private fun performanceClickCancelButton() {
        composeTestRule.onNodeWithText("CANCEL").performClick()
    }

    private fun selectEntreeScreen() {
        composeTestRule.onNodeWithStringId(R.string.start_order).performClick()
        composeTestRule.onNodeWithText(DataSource.entreeMenuItems[0].name).performClick()
        composeTestRule.onNodeWithText(DataSource.entreeMenuItems[1].name).performClick()
    }

    private fun navigateToSideDishScreen() {
        selectEntreeScreen()
        performanceClickNextButton()
    }

    private fun navigateToAccompanimentScreen() {
        navigateToSideDishScreen()
        composeTestRule.onNodeWithText(DataSource.sideDishMenuItems[0].name).performClick()
        performanceClickNextButton()
    }

    private fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
    }

    @Test
    fun lunchTrayNavHost_clickNextOnStartOrderScreen_navigatesToSideDishScreen() {
        navigateToSideDishScreen()
        navController.assertCurrentRouteName(LunchTrayScreen.SideDishMenu.name)
    }

    @Test
    fun lunchTrayNavHost_clickBackOnSideDishScreen_navigatesToStartOrderScreen() {
        navigateToSideDishScreen()
        performNavigateUp()
        navController.assertCurrentRouteName(LunchTrayScreen.EntreeMenu.name)
    }

    @Test
    fun lunchTrayNavHost_clickCancelOnSideDishScreen_navigatesToStartOrderScreen() {
        navigateToSideDishScreen()
        performanceClickCancelButton()
        navController.assertCurrentRouteName(LunchTrayScreen.Start.name)
    }

    @Test
    fun lunchTrayNavHost_clickNextOnSideDishScreen_navigatesToAccompanimentScreen() {
        navigateToAccompanimentScreen()
        performanceClickNextButton()
        navController.assertCurrentRouteName(LunchTrayScreen.AccompanimentMenu.name)
    }

    @Test
    fun lunchTrayNavHost_clickBackOnAccompanimentScreen_navigatesToStartScreen() {
        navigateToAccompanimentScreen()
        performNavigateUp()
        navController.assertCurrentRouteName(LunchTrayScreen.SideDishMenu.name)
    }

    @Test
    fun lunchTrayNavHost_clickCancelOnAccompaniment_navigatesToStartOrderScreen() {
        navigateToAccompanimentScreen()
        performanceClickCancelButton()
        navController.assertCurrentRouteName(LunchTrayScreen.Start.name)
    }

    @Test
    fun lunchTrayNavHost_clickCancelOnSummaryScreen_navigatesToStartOrderScreen() {
        navigateToAccompanimentScreen()
        performanceClickCancelButton()
        navController.assertCurrentRouteName(LunchTrayScreen.Start.name)
    }

}
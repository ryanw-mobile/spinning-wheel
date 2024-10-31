package com.rwmobi.spinningwheel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.rwmobi.spinningwheel.ui.RealisticSpinningWheel
import com.rwmobi.spinningwheel.ui.theme.SpinningWheelAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpinningWheelAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WheelDemo()
                }
            }
        }
    }
}

@Composable
fun WheelDemo() {
    // Example segment labels and colors
    val segments = listOf("A", "B", "C", "D", "E", "F")
    val segmentColors =
        listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta, Color.Cyan)

    // Load a drawable or bitmap resource for the center icon
    // Assuming you have a drawable resource in your project
    val centerIcon = painterResource(R.drawable.ic_launcher_foreground)

    // Handle selected segment when the wheel stops
    val onSegmentSelected: (String) -> Unit = { selectedSegment ->
        // Perform an action with the selected segment
        println("Selected Segment: $selectedSegment")
    }

    // Call the spinning wheel composable
    RealisticSpinningWheel(
        segments = segments,
        segmentColors = segmentColors,
        centerIcon = centerIcon,
        onSegmentSelected = onSegmentSelected
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpinningWheelAppTheme {
        WheelDemo()
    }
}

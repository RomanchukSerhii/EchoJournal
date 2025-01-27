package com.serhiiromanchuk.echojournal.presentation.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentSize
import androidx.glance.unit.ColorProvider
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.presentation.MainActivity
import com.serhiiromanchuk.echojournal.utils.Constants


object EchoWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent()
        }
    }
}

class EchoWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = EchoWidget

}

@Composable
fun WidgetContent() {
    val widgetKey = ActionParameters.Key<Boolean>(Constants.KEY_WIDGET_INTENT)
    Column(
        modifier = GlanceModifier
            .wrapContentSize()
            .background(ColorProvider(Color(0xFFD4E8FF)))
            .clickable(actionStartActivity<MainActivity>(
                actionParametersOf(widgetKey to true)
            ))
    ) {
        Box(
            modifier = GlanceModifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                provider = ImageProvider(R.drawable.ic_widget),
                contentDescription = null,
            )
        }
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 15.dp, bottom = 24.dp)
        ) {
            Image(
                provider = ImageProvider(R.drawable.ic_widget_text),
                contentDescription = null
            )
        }
    }
}
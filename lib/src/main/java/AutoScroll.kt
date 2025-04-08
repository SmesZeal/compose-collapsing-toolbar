import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.CollapsingToolbarState

@Composable
fun AutoScroll(state: CollapsingToolbarScaffoldState, delayMillis: Long = 75) {
	val scope = rememberCoroutineScope()
	val isScrollInProgress = state.toolbarState.isScrollInProgress
	val progress = state.toolbarState.progress

	LaunchedEffect(progress, isScrollInProgress) {
		if (!isScrollInProgress) {
			delay(delayMillis)
			if (!isScrollInProgress && progress in 0f..1f && progress != 0f && progress != 1f) {
				checkProgress(scope, state)
			}
		}
	}
}
fun checkProgress(
	scope: CoroutineScope,
	state: CollapsingToolbarScaffoldState
) {
	val toolbarState = state.toolbarState
	val currentHeight = toolbarState.height
	val midPoint = (toolbarState.maxHeight + toolbarState.minHeight) / 2

	scope.launch {
		val targetHeight = if (currentHeight < midPoint) {
			toolbarState.minHeight
		} else {
			toolbarState.maxHeight
		}
		animateToToolbarHeight(toolbarState, targetHeight)
	}
}

suspend fun animateToToolbarHeight(
	toolbarState: CollapsingToolbarState,
	targetHeight: Int
) {
	val animatable = Animatable(toolbarState.height.toFloat())
	animatable.animateTo(
		targetValue = targetHeight.toFloat(),
		animationSpec = tween(durationMillis = 150)
	) {
		toolbarState.height = value.toInt()
	}
}
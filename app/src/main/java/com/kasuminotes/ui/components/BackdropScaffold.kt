package com.kasuminotes.ui.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Possible values of [BackdropScaffoldState]
 */
enum class BackdropValue {
    /**
     * Indicates the back layer is concealed and the front layer is active.
     */
    Concealed,

    /**
     * Indicates the back layer is revealed and the front layer is inactive.
     */
    Revealed
}

@Stable
class BackdropScaffoldState(
    initialValue: BackdropValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (BackdropValue) -> Boolean = { true },
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
) : SwipeableState<BackdropValue>(
    initialValue = initialValue,
    animationSpec = animationSpec,
    confirmStateChange = confirmStateChange
) {
    /**
     * Whether the back layer is revealed.
     */
    val isRevealed: Boolean
        get() = currentValue == BackdropValue.Revealed

    /**
     * Whether the back layer is concealed.
     */
    val isConcealed: Boolean
        get() = currentValue == BackdropValue.Concealed

    /**
     * Reveal the back layer with animation and suspend until it if fully revealed or animation
     * has been cancelled.  This method will throw [CancellationException] if the animation is
     * interrupted
     *
     * @return the reason the reveal animation ended
     */
    suspend fun reveal() = animateTo(targetValue = BackdropValue.Revealed)

    /**
     * Conceal the back layer with animation and suspend until it if fully concealed or animation
     * has been cancelled. This method will throw [CancellationException] if the animation is
     * interrupted
     *
     * @return the reason the conceal animation ended
     */
    suspend fun conceal() = animateTo(targetValue = BackdropValue.Concealed)

    internal val nestedScrollConnection = this.PreUpPostDownNestedScrollConnection

    companion object{
        /**
         * The default [Saver] implementation for [BackdropScaffoldState].
         */
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (BackdropValue) -> Boolean,
            snackbarHostState: SnackbarHostState
        ): Saver<BackdropScaffoldState, *> = Saver(
            save = { it.currentValue },
            restore = {
                BackdropScaffoldState(
                    initialValue = it,
                    animationSpec = animationSpec,
                    confirmStateChange = confirmStateChange,
                    snackbarHostState = snackbarHostState
                )
            }
        )
    }
}

/**
 * Create and [remember] a [BackdropScaffoldState].
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 * @param snackbarHostState The [SnackbarHostState] used to show snackbars inside the scaffold.
 */
@Composable
fun rememberBackdropScaffoldState(
    initialValue: BackdropValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (BackdropValue) -> Boolean = { true },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): BackdropScaffoldState {
    return rememberSaveable(
        animationSpec,
        confirmStateChange,
        snackbarHostState,
        saver = BackdropScaffoldState.Saver(
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange,
            snackbarHostState = snackbarHostState
        )
    ) {
        BackdropScaffoldState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange,
            snackbarHostState = snackbarHostState
        )
    }
}

/**
 * <a href="https://material.io/components/backdrop" class="external" target="_blank">Material Design backdrop</a>.
 *
 * A backdrop appears behind all other surfaces in an app, displaying contextual and actionable
 * content.
 *
 * ![Backdrop image](https://developer.android.com/images/reference/androidx/compose/material/backdrop.png)
 *
 * This component provides an API to put together several material components to construct your
 * screen. For a similar component which implements the basic material design layout strategy
 * with app bars, floating action buttons and navigation drawers, use the standard [Scaffold].
 * For similar component that uses a bottom sheet as the centerpiece of the screen, use
 * [BottomSheetScaffold].
 *
 * Either the back layer or front layer can be active at a time. When the front layer is active,
 * it sits at an offset below the top of the screen. This is the [peekHeight] and defaults to
 * 56dp which is the default app bar height. When the front layer is inactive, it sticks to the
 * height of the back layer's content if [stickyFrontLayer] is set to `true` and the height of
 * the front layer exceeds the [headerHeight], and otherwise it minimizes to the [headerHeight].
 * To switch between the back layer and front layer, you can either swipe on the front layer if
 * [gesturesEnabled] is set to `true` or use any of the methods in [BackdropScaffoldState].
 *
 * The scaffold also contains an app bar, which by default is placed above the back layer's
 * content. If [persistentAppBar] is set to `false`, then the backdrop will not show the app bar
 * when the back layer is revealed; instead it will switch between the app bar and the provided
 * content with an animation. For best results, the [peekHeight] should match the app bar height.
 * To show a snackbar, use the method `showSnackbar` of [BackdropScaffoldState.snackbarHostState].
 *
 * A simple example of a backdrop scaffold looks like this:
 *
 * @sample androidx.compose.material.samples.BackdropScaffoldSample
 *
 * @param appBar App bar for the back layer. Make sure that the [peekHeight] is equal to the
 * height of the app bar, so that the app bar is fully visible. Consider using [TopAppBar] but
 * set the elevation to 0dp and background color to transparent as a surface is already provided.
 * @param backLayerContent The content of the back layer.
 * @param frontLayerContent The content of the front layer.
 * @param modifier Optional [Modifier] for the root of the scaffold.
 * @param scaffoldState The state of the scaffold.
 * @param gesturesEnabled Whether or not the backdrop can be interacted with by gestures.
 * @param peekHeight The height of the visible part of the back layer when it is concealed.
 * @param headerHeight The minimum height of the front layer when it is inactive.
 * @param persistentAppBar Whether the app bar should be shown when the back layer is revealed.
 * By default, it will always be shown above the back layer's content. If this is set to `false`,
 * the back layer will automatically switch between the app bar and its content with an animation.
 * @param stickyFrontLayer Whether the front layer should stick to the height of the back layer.
 * @param backLayerBackgroundColor The background color of the back layer.
 * @param backLayerContentColor The preferred content color provided by the back layer to its
 * children. Defaults to the matching content color for [backLayerBackgroundColor], or if that
 * is not a color from the theme, this will keep the same content color set above the back layer.
 * @param frontLayerShape The shape of the front layer.
 * @param frontLayerElevation The elevation of the front layer.
 * @param frontLayerBackgroundColor The background color of the front layer.
 * @param frontLayerContentColor The preferred content color provided by the back front to its
 * children. Defaults to the matching content color for [frontLayerBackgroundColor], or if that
 * is not a color from the theme, this will keep the same content color set above the front layer.
 * @param frontLayerScrimColor The color of the scrim applied to the front layer when the back
 * layer is revealed. If the color passed is [Color.Unspecified], then a scrim will not be
 * applied and interaction with the front layer will not be blocked when the back layer is revealed.
 * @param snackbarHost The component hosting the snackbars shown inside the scaffold.
 */
@Composable
fun BackdropScaffold(
    appBar: @Composable () -> Unit,
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
    gesturesEnabled: Boolean = true,
    peekHeight: Dp = BackdropScaffoldDefaults.PeekHeight,
    headerHeight: Dp = BackdropScaffoldDefaults.HeaderHeight,
    persistentAppBar: Boolean = true,
    stickyFrontLayer: Boolean = true,
    backLayerBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    backLayerContentColor: Color = contentColorFor(backLayerBackgroundColor),
    frontLayerShape: Shape = BackdropScaffoldDefaults.frontLayerShape,
    frontLayerElevation: Dp = BackdropScaffoldDefaults.FrontLayerElevation,
    frontLayerBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    frontLayerContentColor: Color = contentColorFor(frontLayerBackgroundColor),
    frontLayerScrimColor: Color = BackdropScaffoldDefaults.frontLayerScrimColor,
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    calcBackLayerConstraints: ((Constraints) -> Constraints)? = null
) {
    val peekHeightPx = with(LocalDensity.current) { peekHeight.toPx() }
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }

    val backLayer = @Composable {
        if (persistentAppBar) {
            Column {
                appBar()
                backLayerContent()
            }
        } else {
            BackLayerTransition(scaffoldState.targetValue, appBar, backLayerContent)
        }
    }
    val calculateBackLayerConstraints: (Constraints) -> Constraints = calcBackLayerConstraints ?: {
        it.copy(minWidth = 0, minHeight = 0).offset(vertical = -headerHeightPx.roundToInt())
    }

    // Back Layer
    Surface(
        color = backLayerBackgroundColor,
        contentColor = backLayerContentColor
    ) {
        val scope = rememberCoroutineScope()
        BackdropStack(
            modifier.fillMaxSize(),
            backLayer,
            calculateBackLayerConstraints
        ) { constraints, backLayerHeight ->
            val fullHeight = constraints.maxHeight.toFloat()
            var revealedHeight = fullHeight - headerHeightPx
            if (stickyFrontLayer) {
                revealedHeight = min(revealedHeight, backLayerHeight)
            }
            val nestedScroll = if (gesturesEnabled) {
                Modifier.nestedScroll(scaffoldState.nestedScrollConnection)
            } else {
                Modifier
            }

            val swipeable = Modifier
                .then(nestedScroll)
                .swipeable(
                    state = scaffoldState,
                    anchors = mapOf(
                        peekHeightPx to BackdropValue.Concealed,
                        revealedHeight to BackdropValue.Revealed
                    ),
                    orientation = Orientation.Vertical,
                    enabled = gesturesEnabled
                )
                .semantics {
                    if (scaffoldState.isConcealed) {
                        collapse {
                            if (scaffoldState.confirmStateChange(BackdropValue.Revealed)) {
                                scope.launch { scaffoldState.reveal() }
                            }; true
                        }
                    } else {
                        expand {
                            if (scaffoldState.confirmStateChange(BackdropValue.Concealed)) {
                                scope.launch { scaffoldState.conceal() }
                            }; true
                        }
                    }
                }

            // Front Layer
            Surface(
                Modifier
                    .offset { IntOffset(0, scaffoldState.offset.value.roundToInt()) }
                    .then(swipeable),
                shape = frontLayerShape,
                color = frontLayerBackgroundColor,
                contentColor = frontLayerContentColor,
                tonalElevation = frontLayerElevation
            ) {
                Box(Modifier.padding(bottom = peekHeight)) {
                    frontLayerContent()
                    Scrim(
                        color = frontLayerScrimColor,
                        onDismiss = {
                            if (gesturesEnabled && scaffoldState.confirmStateChange(BackdropValue.Concealed)) {
                                scope.launch { scaffoldState.conceal() }
                            }
                        },
                        visible = scaffoldState.targetValue == BackdropValue.Revealed
                    )
                }
            }

            // Snackbar host
            Box(
                Modifier
                    .padding(
                        bottom = if (scaffoldState.isRevealed &&
                                revealedHeight == fullHeight - headerHeightPx
                        ) headerHeight else 0.dp
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                snackbarHost(scaffoldState.snackbarHostState)
            }
        }
    }
}

@Composable
private fun Scrim(
    color: Color,
    onDismiss: () -> Unit,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec()
        )
        val dismissModifier = if (visible) {
            Modifier.pointerInput(Unit) { detectTapGestures { onDismiss() } }
        } else {
            Modifier
        }
        Canvas(
            Modifier
                .fillMaxSize()
                .then(dismissModifier)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}

/**
 * A shared axis transition, used in the back layer. Both the [appBar] and the [content] shift
 * vertically, while they crossfade. It is very important that both are composed and measured,
 * even if invisible, and that this component is as large as both of them.
 */
@Composable
private fun BackLayerTransition(
    target: BackdropValue,
    appBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    // The progress of the animation between Revealed (0) and Concealed (2).
    // The midpoint (1) is the point where the appBar and backContent are switched.
    val animationProgress by animateFloatAsState(
        targetValue = if (target == BackdropValue.Revealed) 0f else 2f, animationSpec = TweenSpec()
    )
    val animationSlideOffset = with(LocalDensity.current) { AnimationSlideOffset.toPx() }

    val appBarFloat = (animationProgress - 1).coerceIn(0f, 1f)
    val contentFloat = (1 - animationProgress).coerceIn(0f, 1f)

    Box {
        Box(
            Modifier
                .zIndex(appBarFloat)
                .graphicsLayer(
                    alpha = appBarFloat,
                    translationY = (1 - appBarFloat) * animationSlideOffset
                )
        ) {
            appBar()
        }
        Box(
            Modifier
                .zIndex(contentFloat)
                .graphicsLayer(
                    alpha = contentFloat,
                    translationY = (1 - contentFloat) * -animationSlideOffset
                )
        ) {
            content()
        }
    }
}

@Composable
private fun BackdropStack(
    modifier: Modifier,
    backLayer: @Composable @UiComposable () -> Unit,
    calculateBackLayerConstraints: (Constraints) -> Constraints,
    frontLayer: @Composable @UiComposable (Constraints, Float) -> Unit
) {
    SubcomposeLayout(modifier) {constraints ->
        val backLayerPlaceable =
            subcompose(BackdropLayers.Back, backLayer).first()
                .measure(calculateBackLayerConstraints(constraints))

        val backLayerHeight = backLayerPlaceable.height.toFloat()

        val placeables =
            subcompose(BackdropLayers.Front) {
                frontLayer(constraints, backLayerHeight)
            }.fastMap { it.measure(constraints) }

        var maxWidth = max(constraints.minWidth, backLayerPlaceable.width)
        var maxHeight = max(constraints.minHeight, backLayerPlaceable.height)
        placeables.fastForEach {
            maxWidth = max(maxWidth, it.width)
            maxHeight = max(maxHeight, it.height)
        }

        layout(maxWidth, maxHeight) {
            backLayerPlaceable.placeRelative(0, 0)
            placeables.fastForEach { it.placeRelative(0, 0) }
        }
    }
}

private enum class BackdropLayers { Back, Front }

/**
 * Contains useful defaults for [BackdropScaffold].
 */
object BackdropScaffoldDefaults {

    /**
     * The default peek height of the back layer.
     */
    val PeekHeight = 56.dp

    /**
     * The default header height of the front layer.
     */
    val HeaderHeight = 48.dp

    /**
     * The default shape of the front layer.
     */
    val frontLayerShape: Shape
        @Composable
        get() = BottomSheetDefaults.ExpandedShape

    /**
     * The default elevation of the front layer.
     */
    val FrontLayerElevation = 1.dp

    val frontLayerScrimColor: Color
        @Composable get() = MaterialTheme.colorScheme.scrim.copy(0.15f)
}

private val AnimationSlideOffset = 20.dp

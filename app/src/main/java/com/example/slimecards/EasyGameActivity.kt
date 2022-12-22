package com.example.slimecards

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.Chronometer
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlin.properties.Delegates
/**
 * Activity that manages the view of a game in easy mode, playing with six cards.
 */
class EasyGameActivity : AppCompatActivity(), View.OnClickListener
{
    private lateinit var viewModel : EasyGameViewModel

    private lateinit var chronometer : Chronometer
    private lateinit var timerButton : ImageView

    private lateinit var animatorFront : AnimatorSet
    private lateinit var animatorBack : AnimatorSet

    private lateinit var card1Front : ImageView
    private lateinit var card2Front : ImageView
    private lateinit var card3Front : ImageView
    private lateinit var card4Front : ImageView
    private lateinit var card5Front : ImageView
    private lateinit var card6Front : ImageView

    private lateinit var card1Back : ImageView
    private lateinit var card2Back : ImageView
    private lateinit var card3Back : ImageView
    private lateinit var card4Back : ImageView
    private lateinit var card5Back : ImageView
    private lateinit var card6Back : ImageView

    private var milliseconds by Delegates.notNull<Long>()
    /**
     * Sets a card instantly facing up, using alpha.
     * It's used to restore a card state when the method onResume() is executed.
     * @param frontCard The representative visual side of the card to turn up.
     * @param backCard The specific back View of the card to turn up.
     */
    private fun restore( frontCard : View, backCard : View )
    {
        frontCard.alpha = 1F
        backCard.alpha = 0F
    }
    /**
     * Sets all the cards instantly facing up if their Boolean state returns true.
     */
    private fun restore()
    {
        if ( viewModel.getCards()[0].turnedUp ) restore( card1Front, card1Back )
        if ( viewModel.getCards()[1].turnedUp ) restore( card2Front, card2Back )
        if ( viewModel.getCards()[2].turnedUp ) restore( card3Front, card3Back )
        if ( viewModel.getCards()[3].turnedUp ) restore( card4Front, card4Back )
        if ( viewModel.getCards()[4].turnedUp ) restore( card5Front, card5Back )
        if ( viewModel.getCards()[5].turnedUp ) restore( card6Front, card6Back )
    }
    /**
     * Initializes an Intent to start the score activity,
     * introduces the difficulty of this view, the period of time passed and the score obtained.
     * Waits one thousand and five hundred milliseconds before start the activity, so the last card chosen has time to be animated.
     */
    private fun goToScore()
    {
        milliseconds = SystemClock.elapsedRealtime() - chronometer.base
        val score = Intent( this, ScoreActivity::class.java )
        score.putExtra( "difficulty", "easy" )
        score.putExtra( "time", milliseconds.toString() )
        score.putExtra( "score", viewModel.getScore().toString() )
        Handler( Looper.getMainLooper() ).postDelayed({ startActivity( score ) }, 1500)
    }
    /**
     * Recursive method, first waits five hundred and fifty milliseconds.
     * Then, turns down a card from its two views and id.
     * This method must first be called with the id zero, and then its recalled with id one.
     * When its called with id one, restarts the view matrix so it doesn't host any view.
     * @param id The unique identifier of the card to turn down.
     */
    private fun delayedTurnDown( id : Int )
    {
        Handler( Looper.getMainLooper() ).postDelayed(
            {
                turnDown( viewModel.getViewMatrix()[ id ][0] as View, viewModel.getViewMatrix()[ id ][1] as View, viewModel.getViewMatrix()[ id ][2] as Int )
                if ( id == 0 ) delayedTurnDown( 1 )
                else viewModel.setViewMatrix( mutableListOf() )
            }, 550)
    }
    /**
     * Returns true if the resource identifier of the two first cards in the view matrix are the same, false otherwise.
     */
    private fun areEquals() : Boolean
    {
        return viewModel.getCards()[ viewModel.getViewMatrix()[0][2] as Int ].resourceId == viewModel.getCards()[ viewModel.getViewMatrix()[1][2] as Int ].resourceId
    }
    /**
     * Inserts an array containing two views and an id to the view matrix
     * and if there are two arrays in the view matrix but their resource identifiers don't match
     * sets viewModel.isPair to false and makes the user wait until the two last cards are turned down again.
     * Else, sets the boolean to true, restarts the view matrix (in the other case, it's restarted in this.delayedTurnDown() method),
     * increases viewModel.pairCount by one and increases the current score depending on the current time.
     * @param frontCard The representative visual side of a card as a View.
     * @param backCard The specific back View of a card.
     * @param id The unique identifier of a card as an Int.
     * @param time The period of time passed since the beginning of the game until the card is clicked in milliseconds.
     */
    private fun selectCard( frontCard : View, backCard : View, id : Int, time : Long )
    {
        viewModel.getViewMatrix().add( arrayOf( frontCard, backCard, id ) )

        if ( viewModel.getViewMatrix().size == 2 && !areEquals() )
        {
            viewModel.setIsPairState( false )
            delayedTurnDown( 0 )
        } else if ( viewModel.getViewMatrix().size == 2 )
        {
            viewModel.setIsPairState( true )
            viewModel.setViewMatrix( mutableListOf() )
            viewModel.increasePairCount()
            viewModel.addScore( time )
        }
    }
    /**
     * Turns down a card.
     * @param frontCard The representative visual side of the card to turn down.
     * @param backCard The specific back View of the card to turn down.
     * @param id The unique identifier of the card to turn down.
     */
    private fun turnDown( frontCard : View, backCard : View, id : Int )
    {
        animatorBack.setTarget( backCard )
        animatorFront.setTarget( frontCard )
        animatorFront.start()
        animatorBack.start()
        viewModel.getCards()[id].turnedUp = false
    }
    /**
     * Disables the clickable property of all cards, turns up a card and allows it again.
     * @param frontCard The representative visual side of the card to turn up.
     * @param backCard The specific back View of the card to turn up.
     * @param id The unique identifier of the card to turn up.
     * @param time The period of time passed since the beginning of the game until the card is clicked in milliseconds.
     */
    private fun turnUp( frontCard : View, backCard : View, id : Int, time : Long )
    {
        viewModel.setClickableState( false )

        animatorBack.setTarget( frontCard )
        animatorFront.setTarget( backCard )
        animatorBack.start()
        animatorFront.start()
        viewModel.getCards()[id].turnedUp = true

        selectCard( frontCard, backCard, id, time )
        viewModel.enableClick()
    }
    /**
     * If the game is not in pause, the cards are clickable and the card is turned down, turns up a card.
     * @param frontCard The representative visual side of the card to turn up.
     * @param backCard The specific back View of the card to turn up.
     * @param id The unique identifier of the card to turn up.
     * @param time The period of time passed since the beginning of the game until the card is clicked in milliseconds.
     */
    private fun animate( frontCard : View, backCard : View, id : Int, time : Long )
    {
        if ( viewModel.ableToTurnUp( id ) ) turnUp( frontCard, backCard, id, time )
    }
    /**
     * Clicks a card and if there are three turned up pairs, sets the pair value to zero
     * to dodge this condition again and starts the score activity.
     * @param view The specific back View of the card to turn up.
     */
    private fun clickCard( view : View? )
    {
        val milliseconds = SystemClock.elapsedRealtime() - chronometer.base
        when ( view )
        {
            card1Back -> animate( card1Front, card1Back, 0, milliseconds )
            card2Back -> animate( card2Front, card2Back, 1, milliseconds )
            card3Back -> animate( card3Front, card3Back, 2, milliseconds )
            card4Back -> animate( card4Front, card4Back, 3, milliseconds )
            card5Back -> animate( card5Front, card5Back, 4, milliseconds )
            card6Back -> animate( card6Front, card6Back, 5, milliseconds )
        }
        if ( viewModel.getPairValue() == 3 )
        {
            viewModel.setPairValue( 0 )
            goToScore()
        }
    }
    /**
     * Clicks a card and turns it up.
     * @param view The specific back View of the card to turn up.
     */
    override fun onClick( view : View? )
    {
        clickCard( view )
    }
    /**
     * Sets clicks listeners for all the cards.
     */
    private fun setBackOnClickListeners()
    {
        card1Back.setOnClickListener( this )
        card2Back.setOnClickListener( this )
        card3Back.setOnClickListener( this )
        card4Back.setOnClickListener( this )
        card5Back.setOnClickListener( this )
        card6Back.setOnClickListener( this )
    }
    /**
     * Sets the resource identifier for all the cards.
     */
    private fun setFrontImageResources()
    {
        card1Front.setImageResource( viewModel.getCards()[0].resourceId )
        card2Front.setImageResource( viewModel.getCards()[1].resourceId )
        card3Front.setImageResource( viewModel.getCards()[2].resourceId )
        card4Front.setImageResource( viewModel.getCards()[3].resourceId )
        card5Front.setImageResource( viewModel.getCards()[4].resourceId )
        card6Front.setImageResource( viewModel.getCards()[5].resourceId )
    }
    /**
     * Initializes the back side of all the cards.
     */
    private fun initializeBackCards()
    {
        card1Back = findViewById( R.id.firstCardEasyBack )
        card2Back = findViewById( R.id.secondCardEasyBack )
        card3Back = findViewById( R.id.thirdCardEasyBack )
        card4Back = findViewById( R.id.fourthCardEasyBack )
        card5Back = findViewById( R.id.fifthCardEasyBack )
        card6Back = findViewById( R.id.sixthCardEasyBack )
    }
    /**
     * Initializes the front side of all the cards.
     */
    private fun initializeFrontCards()
    {
        card1Front = findViewById( R.id.firstCardEasyFront )
        card2Front = findViewById( R.id.secondCardEasyFront )
        card3Front = findViewById( R.id.thirdCardEasyFront )
        card4Front = findViewById( R.id.fourthCardEasyFront )
        card5Front = findViewById( R.id.fifthCardEasyFront )
        card6Front = findViewById( R.id.sixthCardEasyFront )
    }
    /**
     * Changes the background resource identifier depending on if the game is paused or not.
     */
    private fun updateTimerButton()
    {
        if ( viewModel.running() ) timerButton.setBackgroundResource( R.drawable.chronos_stop )
        else timerButton.setBackgroundResource( R.drawable.chronos_run )
    }
    /**
     * Resumes the chronometer by updating the base and changing the boolean state to true.
     */
    private fun resume()
    {
        chronometer.base = SystemClock.elapsedRealtime() - viewModel.getPauseOffSet()
        chronometer.start()
        viewModel.setRunningStatus( true )
    }
    /**
     * Stops the chronometer by updating the pause offset and changing the boolean state to false.
     */
    private fun stop()
    {
        chronometer.stop()
        viewModel.setPauseOffSet( SystemClock.elapsedRealtime() - chronometer.base )
        viewModel.setRunningStatus( false )
    }
    /**
     * If the game is not paused, stops the chronometer. Else, resumes it.
     */
    private fun switchRunningStatus()
    {
        if ( viewModel.running() ) stop()
        else resume()
    }
    /**
     * Initializes the chronometer and sets a click listener for the timer button.
     */
    private fun initializeChronometer()
    {
        timerButton = findViewById( R.id.stopTimeEasy )
        chronometer = findViewById( R.id.chronometerEasy )

        if ( viewModel.running() ) chronometer.start()

        timerButton.setOnClickListener {
            YoYo.with( Techniques.Tada ).duration( 700 ).playOn( timerButton )
            switchRunningStatus()
            updateTimerButton()
        }
    }
    /**
     * Initializes all the animators.
     */
    private fun initializeAnimators()
    {
        animatorFront = AnimatorInflater.loadAnimator( this, R.animator.card_animator_front ) as AnimatorSet
        animatorBack = AnimatorInflater.loadAnimator( this, R.animator.card_animator_back ) as AnimatorSet
    }
    /**
     * Initializes the view model.
     */
    private fun initializeViewModel()
    {
        viewModel = ViewModelProvider( this ).get( EasyGameViewModel::class.java )
    }
    /**
     * When the function onCreate(Bundle?) is executed:
     * - Initializes the view model.
     * - Initializes animators.
     * - Initializes the chronometer.
     * - Initializes the front cards.
     * - Initializes the back cards.
     * - Sets the front image resources.
     * - Sets on click listeners for the back cards image views.
     * - If a card is turned up, turns it up instantly.
     */
    override fun onCreate( savedInstanceState : Bundle? )
    {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_easy_game )

        initializeViewModel()

        initializeAnimators()

        initializeChronometer()

        initializeFrontCards()

        initializeBackCards()

        setFrontImageResources()

        setBackOnClickListeners()

        restore()
    }
    /**
     * When the function onResume() is executed, if the game is not paused resumes it, otherwise updates the chronometer base.
     * Then updates the timer button appearance.
     */
    override fun onResume()
    {
        super.onResume()
        if ( viewModel.running() ) resume()
        else chronometer.base = viewModel.getBase()
        updateTimerButton()
    }
    /**
     * When the function onPause() is executed, saves the base in the view model, stops the chronometer and saves the pause offset.
     */
    override fun onPause()
    {
        viewModel.setBase( chronometer.base + 2000 )
        chronometer.stop()
        super.onPause()
        viewModel.setPauseOffSet( SystemClock.elapsedRealtime() - chronometer.base )
    }
}
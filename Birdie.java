/*
------------------------------------------------------------------------------------------------------------------------------------------

Authors: Talha.I - Saad.M

Date Created: May 28th 2019

Created for the ICS3U1 Summative in the year of 2018-2019

------------------------------------------------------------------------------------------------------------------------------------------

Hotkeys:

Known Bugs:
+ Background takes time to load as soon as game starts.
+ Screen Flickers - due to Thread.sleep function.

------------------------------------------------------------------------------------------------------------------------------------------

*/

//Libraries / Statements
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Button;


public class Birdie extends Applet implements Runnable, MouseListener, MouseMotionListener, KeyListener, ActionListener
{
    // Variables that store audioclips for backgroudn music
    AudioClip BackgroundMsc;
    AudioClip clickSound;
    AudioClip wing;
    AudioClip die;
    AudioClip point;

    // Variables for images
    Image Background; //first background img
    Image NightBackground;
    Image CyanBackground;
    Image birds; // All the birds for header of main menu
    Image Bottom; // backroung for footer
    Image Title; // title
    Image addPipes; // pipees
    Image topPipes; // top of pipes

    //Birds Images - All Bird sizes are 80 by 62 px
    Image BlueDetective;
    Image GreenDetective;
    Image BumbleBee;
    Image RedHawk;
    Image Chosen; // User's Bird Choice
    Image choose;
    Image credits;

    //Other Images
    Image Controls;
    Image Lose;
    Image space;
    Image restart;
    Image menu;
    Image greenscore;
    Image easyModeButton;

    //Button Images
    Image creditsButton; // The credits button image
    Image playButton; // The play button image




    //Boolean variables for menus
    boolean StartMenu = true; //the game main menu
    boolean SelectMenu = false; //the charecter select menu
    boolean Game = false; // The game
    boolean GameOverMenu = false; //the game over menu
    boolean CreditsMenu = false; // the credits menu
    boolean easyMode = false;
    boolean music = true;

    //Boolean variables for Charecters
    boolean BlueDetectiveChosen = false;
    boolean RedHawkChosen = false;
    boolean BumbleBeeChosen = false;
    boolean GreenDetectiveChosen = false;


    //Boolean variable to clear screen
    boolean ClearScreen = false;

    //variables
    int bx1 = 0; // x coordinate for first background img
    int bx2 = 1200; // x coordinate for second background img

    //for bird movement
    int i;
    int x = 600;
    int y = 400;
    double vy; // velocity of bird

    //buffer variables
    private Image dbImage;
    private Graphics dbg;

    //Other
    int xpos[] = {1400, 1700, 2000, 2300, 2600};  //pipes x pos
    int ybpos[] = {570, 670, 370, 370, 470}; // height of bottom pipes
    int ytpos[] = {300, 400, 100, 100, 200}; // height of top pipes

    int ybselect[] = {570, 670, 370, 370, 470}; // height of bottom pipes
    int ytselect[] = {300, 400, 100, 100, 200}; // height of top pipes
    int yp;
    int height = 0;
    int score = 0;
    int highscore = 0;
    int ezhighscore = 0;
    String xscore;
    String xhighscore;
    String yhighscore;
    int increment = 1400;
    int end = 0;



    public void init ()  // ************************************************* Runnable Methods ********************************************
    {
	setSize (1200, 800); //Resizing the applet dimensions

	//prepare images
	Title = getImage (getDocumentBase (), "Title.png");
	Background = getImage (getDocumentBase (), "Background.png");
	creditsButton = getImage (getDocumentBase (), "creditsButton.png");
	playButton = getImage (getDocumentBase (), "playButton.png");
	birds = getImage (getDocumentBase (), "birds.png");
	Bottom = getImage (getDocumentBase (), "Bottom.png");
	Controls = getImage (getDocumentBase (), "Controls.png");
	Lose = getImage (getDocumentBase (), "Lose.png");
	addPipes = getImage (getDocumentBase (), "addPipes.png");
	topPipes = getImage (getDocumentBase (), "topPipes.png");
	space = getImage (getDocumentBase (), "space.png");
	restart = getImage (getDocumentBase (), "restart.png");
	menu = getImage (getDocumentBase (), "menu.png");
	choose = getImage (getDocumentBase (), "choose.png");
	credits = getImage (getDocumentBase (), "credits.png");
	greenscore = getImage (getDocumentBase (), "greenscore.png");
	NightBackground = getImage (getDocumentBase (), "NightBackground.png");
	CyanBackground = getImage (getDocumentBase (), "CyanBackground.png");
	easyModeButton = getImage (getDocumentBase (), "easyMode.png");



	RedHawk = getImage (getDocumentBase (), "RedHawk.png");
	BlueDetective = getImage (getDocumentBase (), "BlueDetective.png");
	GreenDetective = getImage (getDocumentBase (), "GreenDetective.png");
	BumbleBee = getImage (getDocumentBase (), "BumbleBee.png");

	//Prepare audio clips
	BackgroundMsc = getAudioClip (getCodeBase (), "BackgroundMsc.wav");
	clickSound = getAudioClip (getCodeBase (), "ClickSound.wav");
	wing = getAudioClip (getCodeBase (), "sfx_wing.wav");
	die = getAudioClip (getCodeBase (), "sfx_die.wav");
	point = getAudioClip (getCodeBase (), "sfx_point.wav");


	//Add listners and threads
	addMouseListener (this);
	addMouseMotionListener (this);
	addKeyListener (this);
	Thread thread = new Thread (this);
	thread.start ();
    }


    public void start ()
    {
	BackgroundMsc.loop ();
    }


    public void run ()
    {
	gameMenu (); // Call the game menu method

	charSelect (); // Call the charecter select menu method

	while (true)
	{
	    repaint ();
	    try
	    {
		Thread.sleep (50);
	    }
	    catch (InterruptedException e)
	    {
		e.printStackTrace ();
	    }
	    if (Game)
	    {
		if (GameOverMenu)
		    ResetVal ();



		if (easyMode)
		{
		    bx1 -= 5;
		    bx2 -= 5;

		}
		else
		{
		    bx1 -= 10;
		    bx2 -= 10;
		}

		if (bx1 + 1150 <= 0)
		    bx1 = 1201;
		if (bx2 + 1150 <= 0)
		    bx2 = 1201;

		PointCheck ();




		for (int i = 0 ; i < xpos.length ; i++) // checks if needed to gen new pipe
		{
		    if (xpos [i] <= end)
		    {

			xpos [i] = xPipes ();

			yp = (int) ((Math.random () * 99) + 1);

			if (yp % 4 == 0)
			    yp = 4;
			else
			    if (yp % 3 == 0)
				yp = 3;
			    else
				if (yp % 2 == 0)
				    yp = 2;
				else
				    if (yp > 50)
					yp = 1;
				    else
					yp = 0;



			ybpos [i] = byPipes (yp);
			ytpos [i] = tyPipes (yp);
		    }
		}


		GameOverCheck ();

	    }
	}
    }


    public void stop ()
    {
	BackgroundMsc.stop ();
	System.exit (0);

    }


    public void destroy ()  // *********************************************** Runnable Methods *******************************************
    {

    }



    public void keyPressed (KeyEvent e)  // *************************************** Key event Methods *************************************
    {
	int key = e.getKeyCode (); // stores ACIIcode values into int variable

	if (key == KeyEvent.VK_ESCAPE) // exit game if escape key pressed
	    System.exit (0);

	if (Game)
	{
	    if (key == KeyEvent.VK_SPACE)
	    { // exit game if escape key pressed

		vy += -13;

		WingSound ();
	    }

	}
    }


    public void keyReleased (KeyEvent e)
    {
    }


    public void keyTyped (KeyEvent e)  // *************************************** Key event Methods ***************************************
    {
    }



    public void mouseClicked (MouseEvent e)  // ************************************ Mouse event Methods **********************************
    {
    }


    public void mousePressed (MouseEvent e)
    {
	if (StartMenu) // Main menu buttons
	{
	    easyMode = false;


	    if ((e.getX () >= 450 && e.getX () <= 750) && (e.getY () >= 300 && e.getY () <= 400)) // Clicked the Start Game button
	    {

		playClickSound ();
		StartMenu = false;

		repaint ();

		SelectMenu = true;

		easyMode = false;
		end = -90;
	    }

	    else if ((e.getX () >= 450 && e.getX () <= 750) && (e.getY () >= 425 && e.getY () <= 525)) // Clicked the Credits button
	    {
		playClickSound ();
		StartMenu = false;

		repaint ();


		CreditsMenu = true;

	    }
	    else if ((e.getX () >= 450 && e.getX () <= 750) && (e.getY () >= 550 && e.getY () <= 650)) // Clicked the Credits button
	    {
		playClickSound ();
		StartMenu = false;
		easyMode = true;

		repaint ();


		SelectMenu = true;


		xpos [0] = 1400;
		xpos [1] = 1900;
		xpos [2] = 2300;
		xpos [3] = 2800;
		xpos [4] = 3300;
		end = -1400;




	    }


	}
	else
	    if (SelectMenu) // Selection menu buttons
	    {
		if ((e.getX () >= 470 && e.getX () <= 599) && (e.getY () >= 275 && e.getY () <= 375))
		{
		    playClickSound ();
		    BlueDetectiveChosen = true;
		    Chosen = BlueDetective;
		    SelectMenu = false;

		    repaint ();


		    Game = true;
		}
		else
		    if ((e.getX () >= 601 && e.getX () <= 729) && (e.getY () >= 275 && e.getY () <= 375))
		    {
			playClickSound ();
			GreenDetectiveChosen = true;
			Chosen = GreenDetective;
			SelectMenu = false;


			repaint ();


			Game = true;
		    }
		    else
			if ((e.getX () >= 340 && e.getX () <= 469) && (e.getY () >= 275 && e.getY () <= 375))
			{
			    playClickSound ();
			    BumbleBeeChosen = true;
			    Chosen = BumbleBee;
			    SelectMenu = false;


			    repaint ();

			    Game = true;
			}
			else
			    if ((e.getX () >= 730 && e.getX () <= 859) && (e.getY () >= 275 && e.getY () <= 375))
			    {
				playClickSound ();
				RedHawkChosen = true;
				Chosen = RedHawk;
				SelectMenu = false;


				repaint ();

				Game = true;
			    }
	    }
	    else if (GameOverMenu) // game over menu buttons
	    {
		if ((e.getX () >= 550 && e.getX () <= 850) && (e.getY () >= 300 && e.getY () <= 400)) // Clicked the Restart Game button
		{

		    playClickSound ();
		    GameOverMenu = false;

		    ClearScreen = true;
		    ResetVal ();
		    Game = true;

		    repaint ();
		    ClearScreen = false;

		}
		if ((e.getX () >= 550 && e.getX () <= 850) && (e.getY () >= 425 && e.getY () <= 525)) // Clicked the Menu button
		{
		    playClickSound ();
		    GameOverMenu = false;
		    ResetVal ();

		    StartMenu = true;


		    // Clear Screen Images
		    ClearScreen = true;
		    repaint ();
		    ClearScreen = false;

		}


	    }
	    else
		if (CreditsMenu) // credits menu buttons
		{
		    if ((e.getX () >= 900 && e.getX () <= 1200) && (e.getY () >= 0 && e.getY () <= 300)) // Clicked the Credits button
		    {
			playClickSound ();
			CreditsMenu = false;
			StartMenu = true;


			// Clear Screen Images
			ClearScreen = true;
			repaint ();
			ClearScreen = false;

		    }

		}

    }


    public void mouseEntered (MouseEvent e)
    {
	//does nothing
    }


    public void mouseExited (MouseEvent e)
    {
	//does nothing
    }


    public void mouseReleased (MouseEvent e)
    {
    }


    public void mouseMoved (MouseEvent e)
    {
    }


    public void mouseDragged (MouseEvent e)  // ************************************ Mouse event Methods **********************************
    {
    }




    public void update (Graphics g)
    {
	// initialize buffer
	if (dbImage == null)
	{
	    dbImage = createImage (this.getSize ().width, this.getSize ().height);
	    dbg = dbImage.getGraphics ();
	}

	// clear screen in background
	dbg.setColor (getBackground ());
	dbg.fillRect (0, 0, this.getSize ().width, this.getSize ().height);

	// draw elements in background
	dbg.setColor (getForeground ());
	paint (dbg);

	// draw image on the screen
	g.drawImage (dbImage, 0, 0, this);
    }



    public void paint (Graphics g)  // ******************************************* Paint Method *******************************************
    {
	if (score < 10)
	    g.drawImage (Background, 0, 0, this);
	else if (score <= 15)
	    g.drawImage (CyanBackground, 0, 0, this);
	else if (score <= 25)
	    g.drawImage (NightBackground, 0, 0, this);
	else if (score <= 35)
	    g.drawImage (Background, 0, 0, this);
	else if (score <= 40)
	    g.drawImage (CyanBackground, 0, 0, this);
	else if (score <= 50)
	    g.drawImage (NightBackground, 0, 0, this);

	if (ClearScreen) // Clear pictures
	{
	    g.clearRect (0, 0, 1200, 800);
	}

	if (StartMenu) // Main menu pictures
	{
	    BackgroundMsc.loop ();
	    g.drawImage (Title, 400, 50, this);
	    g.drawImage (birds, 430, 175, this);
	    g.drawImage (playButton, 450, 300, this);
	    g.drawImage (creditsButton, 450, 425, this);
	    g.drawImage (easyModeButton, 450, 550, this);
	}

	if (CreditsMenu) // Credits pictures
	{
	    g.drawImage (credits, 0, 0, this);
	    g.drawImage (menu, 900, 0, this);
	    /*Font f = new Font ("Comic Sans MS", Font.PLAIN, 11);
	    Font f1 =ai i won new Font ("Comic Sans MS", Font.PLAIN, 22);
	    g.setColor (Color.BLACK);

	    g.setFont (f1);
	    g.drawString ("Works Cited", 500, 50);

	    g.setFont (f);
	    g.drawString ("Difference between Thread.start() and Thread.run() in Java - GeeksforGeeks. (2019). GeeksforGeeks. Retrieved 9 June 2019, from https://www.geeksforgeeks.org/difference-between-thread-start-and-thread-run-in-java/", 20, 100);
	    g.drawString ("MediaTracker (Java Platform SE 7 ). (2019). Docs.oracle.com. Retrieved 9 June 2019, from https://docs.oracle.com/javase/7/docs/api/java/awt/MediaTracker.html", 20, 120);
	    g.drawString ("Java Applet Basics - GeeksforGeeks. (2017). GeeksforGeeks. Retrieved 9 June 2019, from https://www.geeksforgeeks.org/java-applet-basics/", 20, 140);
	    g.drawString ("|. (2019). Adding Sound To An Applet using AudioClip Class. Ecomputernotes.com. Retrieved 9 June 2019, from https://ecomputernotes.com/java/awt-and-applets/adding-sound-to-an-applet", 20, 160);
	    g.drawString ("Java AWT Reference. (2019). O'Reilly | Safari. Retrieved 9 June 2019, from https://www.oreilly.com/library/view/java-awt-reference/9781565922402/06_chapter-03.html", 20, 180);
	    g.drawString ("Java Examples - write to a file using Applet. (2019). www.tutorialspoint.com. Retrieved 9 June 2019, from https://www.tutorialspoint.com/javaexamples/applet_writefile.htm", 20, 200);
	    g.drawString ("Java Examples - Read a file using Applet. (2019). www.tutorialspoint.com. Retrieved 9 June 2019, from https://www.tutorialspoint.com/javaexamples/applet_readfile.htm", 20, 220);
	    */
	}

	if (SelectMenu) // Charecter Selection pictures
	{
	    g.drawImage (BlueDetective, 470, 275, this);
	    g.drawImage (GreenDetective, 600, 275, this);
	    g.drawImage (BumbleBee, 340, 275, this);
	    g.drawImage (RedHawk, 730, 275, this);
	    g.drawImage (Controls, 445, 390, this);
	    g.drawImage (space, 205, 390, this);
	    g.drawImage (choose, 75, 90, this);
	}


	if (Game) // Game pictures
	{
	    BackgroundMsc.stop ();
	    g.drawImage (Bottom, bx1, 720, this);
	    g.drawImage (Bottom, bx2, 720, this);

	    g.drawImage (Chosen, x, y, this);


	    for (int i = 0 ; i < xpos.length ; i++)
	    {
		height = ybpos [i];

		while (height <= 670)
		{
		    g.drawImage (addPipes, xpos [i], height, this);
		    height += 50;
		}
		g.drawImage (topPipes, xpos [i] - 5, ybpos [i] - 40, this);


		height = ytpos [i];

		while (height >= 0)
		{
		    g.drawImage (addPipes, xpos [i], height, this);
		    g.drawImage (addPipes, xpos [i], height, this);
		    height -= 50;
		}
		g.drawImage (topPipes, xpos [i] - 5, ytpos [i] + 40, this);

	    }
	    Font f1 = new Font ("Comic Sans MS", Font.BOLD, 40);
	    g.setColor (Color.YELLOW);

	    g.setFont (f1);

	    xscore = Integer.toString (score);

	    g.drawString (xscore, 630, 600);

	}



	else if (GameOverMenu)
	{
	    g.drawImage (Lose, -125, 50, this);
	    g.drawImage (restart, 650, 300, this);
	    g.drawImage (menu, 650, 425, this);



	    Font f1 = new Font ("Comic Sans MS", Font.BOLD, 40);
	    g.setColor (Color.BLACK);

	    g.setFont (f1);

	    xscore = "SCORE: " + Integer.toString (score);

	    if (easyMode)
	    {
		if (score > ezhighscore)
		    ezhighscore = score;

		g.drawImage (greenscore, 200, 300, this);
		g.drawString (xscore, 230, 450);

		yhighscore = "HIGHSCORE: " + Integer.toString (ezhighscore);
		g.drawString (yhighscore, 230, 380);
		g.setColor (Color.RED);
		g.drawString ("EASY MODE", 230, 503);
	    }
	    else
	    {
		if (score > highscore)
		    highscore = score;

		g.drawImage (greenscore, 200, 300, this);
		g.drawString (xscore, 230, 450);

		xhighscore = "HIGHSCORE: " + Integer.toString (highscore);
		g.drawString (xhighscore, 230, 380);
		


	    }





	}

    }



    public void actionPerformed (ActionEvent e)  // ************************************** Action Method **********************************
    {

    }



    public void gameMenu ()  // *********************************************** Game Menu Method ******************************************
    {
	repaint (); // draw main menu

	while (StartMenu)
	{
	    try
	    {
		Thread.sleep (600);
	    }
	    catch (InterruptedException e)
	    {
		e.printStackTrace ();
	    }
	}

    }



    public void charSelect ()  // ********************************************* charSelect Method *****************************************
    {
	repaint (); // draw the charecters to select

	while (SelectMenu) // to slow down the loop and stop flickering
	{
	    try
	    {
		Thread.sleep (1000);
	    }
	    catch (InterruptedException e)
	    {
		e.printStackTrace ();
	    }
	}
    }



    public void GameOverCheck ()
    {

	//IF STATEMENT IS TO PREVENT BIRD FROM FALLING OFF SCREEN

	if (y < (658) && y > 0) // Remember (height - 222)
	{
	    if (easyMode)
		vy += 0.75;
	    else
		vy += 1;

	    y += vy;
	}
	else
	{
	    GameOverMenu = true;
	    Game = false;
	    DieSound ();
	}
	//tp 90 40  bd 80 62
	for (int i = 0 ; i < xpos.length ; i++)
	{
	    if ((x + 80 >= xpos [i] - 5) && (x + 80 <= xpos [i] + 95) || (x >= xpos [i] - 5) && (x <= xpos [i] + 95))
	    {
		if (y + 62 >= ybpos [i] - 40) // collision with top pipe
		{
		    GameOverMenu = true;
		    Game = false;
		    DieSound ();
		    break;
		}

		if (y <= ytpos [i] + 40) // collision with bottom pipe
		{
		    GameOverMenu = true;
		    Game = false;
		    DieSound ();
		    break;
		}
	    }
	}

    }



    public void playClickSound ()  // ******************************************* Click Sound Method **************************************
    {
	clickSound.play ();
    }


    public void WingSound ()
    {
	wing.play ();
    }


    public void DieSound ()
    {
	die.play ();
    }


    public void PointSound ()
    {
	point.play ();
    }


    public void ResetVal ()  // Resets All Values
    {
	//variables
	bx1 = 0; // x coordinate for first background img
	bx2 = 1200; // x coordinate for second background img

	//for bird movement
	i = 0;
	x = 600;
	y = 400;
	vy = 0; // velocity of bird

	//Other
	xpos [0] = 1400;
	xpos [1] = 1700;
	xpos [2] = 2000;
	xpos [3] = 2300;
	xpos [4] = 2600;

	if (easyMode)
	{
	    xpos [0] = 1400;
	    xpos [1] = 1900;
	    xpos [2] = 2300;
	    xpos [3] = 2800;
	    xpos [4] = 3300;
	}

	height = 0;
	score = 0;
    }


    public void PointCheck ()
    {

	for (i = 0 ; i < xpos.length ; i++) // move the pipes back
	{

	    if (easyMode)
		xpos [i] -= 5;
	    else
		xpos [i] -= 10;

	    if (xpos [i] == 600)
	    {
		score++;
		PointSound ();

	    }
	}


    }


    public int xPipes ()
    {
	int xp;

	if (easyMode)
	    xp = 1200;
	else
	    xp = 1500;

	return xp;
    }


    public int byPipes (int yp)
    {
	yp = ybselect [yp];
	return yp;
    }



    public int tyPipes (int yp)
    {
	yp = ytselect [yp];
	return yp;
    }



} //Main Method

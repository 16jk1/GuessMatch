/*******************************
 * Name: Joonsoo Kim			*
 * Student ID: 20024108			*
 *******************************/
//Import android tools
package com.example.admin.joonsookim;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
//Import java util
import java.util.Random;
//Import android widget
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;
public class GuessMaster extends AppCompatActivity {
	//Instance variables
    private int numOfEntities;
    private Entity[] entities;
    private int[] tickets;
    private int numOfTickets;
	private int totaltik;
    //Stores Entity
    String entName;
	String answer;
    int entityid = 0;
    int currentTicketWon = 0;
    //View component variabes
    private TextView entityName;
    private TextView ticketsum;
    private Button guessButton;
    private EditText userIn;
    private Button btnclearContent;
    private String userinput;
    private ImageView entityImage;

	//main method that enters the game, and inputs the entities
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//set the xml as the activity UI view
		setContentView(R.layout.activity_guess_activity);
		//specify the button in the view
		guessButton = (Button) findViewById(R.id.btnGuess);
		//EditText for user input
		userIn = (EditText) findViewById(R.id.guessinput);
		//TextView for total tickets
		ticketsum = (TextView) findViewById(R.id.ticket);
		//specify the button in the view
		btnclearContent = (Button) findViewById(R.id.btnClear);
		//TextView for entityName
		entityName = (TextView) findViewById(R.id.entityName);
		// ImageView for entityImage
		entityImage = findViewById(R.id.entityImage);
		//Initializing input entities
		Politician jTrudeau = new Politician("Justin Trudeau", new Date("December", 25, 1971), "Male", "Liberal", 0.25);////
		Singer cDion = new Singer("Celine Dion", new Date("March", 30, 1961), "Female", "La voix du bon Dieu", new Date("November", 6, 1981), 0.5);////
		Person myCreator= new Person("myCreator", new Date("May", 6, 1800), "Male",1);
		Country usa = new Country("United States", new Date("July", 4, 1776), "Washinton D.C.", 0.1);////
		//adding entities
		addEntity(jTrudeau);
		addEntity(cDion);
		addEntity(myCreator);
		addEntity(usa);
		//Choosing random entities
		final int randInd = genRandomEntityId();
		final Entity entity = entities[randInd];
		//OnClick Listener action for clear button
		btnclearContent.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
                changeEntity();
			}
		});
		//OnClick Listener action for submit button
		guessButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
                playGame(entity);
			}
		});
		//set entity name text
		entityName.setText(entity.getName());
		//set image of the entity
		ImageSetter(entity);
		//welcome message
		welcomeToGame(entity);

	}
	//Initializing constructor
	public GuessMaster() {
		numOfEntities = 0;
		entities = new Entity[10]; 
	}
	//Adds a new entity into the array
	public void addEntity(Entity entity) {
//		entities[numOfEntities++] = new Entity(entity);
//		entities[numOfEntities++] = entity;//////
		entities[numOfEntities++] = entity.clone();
	}
	// Given an entity index, use the index to fetch entity from entities
	public void playGame(int entityId) {
		Entity entity = entities[entityId];
		playGame(entity);
	}
	//Method that plays the game
	public void playGame(Entity entity) {
		//Get date of the input entity
		Date bornEnt = entity.getBorn();
		//Get Input from the EdiText
		answer = userIn.getText().toString();
		answer = answer.replace("\n", "").replace("\r", "");
		Date date = new Date(answer);
		//Exits the game if the user inputs "quit"
		if (answer.equals("quit")) {
			System.exit(0);
		}
		//Check User Date Input
		//If user inputs later date than the answer
		if (date.precedes(entity.getBorn())) {
			AlertDialog.Builder laterAlert = new AlertDialog.Builder(GuessMaster.this);
			laterAlert.setTitle("Incorrect!");
			laterAlert.setMessage("Try a later date!");
			laterAlert.setCancelable(false); // No Cancel Button

			laterAlert.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which){ dialog.dismiss();}
			});
			//Show Dialog
			AlertDialog dialog = laterAlert.create();
			dialog.show();

		}
		//If user inputs earlier date
		else if (entity.getBorn().precedes(date)) {
			AlertDialog.Builder earlierAlert = new AlertDialog.Builder(GuessMaster.this);
			earlierAlert.setTitle("Incorrect!");
			earlierAlert.setMessage("Try an earlier date!");
			earlierAlert.setCancelable(false); // No Cancel Button

			earlierAlert.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialop, int which){ dialop.dismiss();}
			});
			//Show Dialog
			AlertDialog dialog = earlierAlert.create();
			dialog.show();

		}
		//If user inputs no input
		else if(answer.equals("")){
			AlertDialog.Builder earlierAlert = new AlertDialog.Builder(GuessMaster.this);
			earlierAlert.setTitle("Incorrect!");
			earlierAlert.setMessage("No Input! Try again!");
			earlierAlert.setCancelable(false); // No Cancel Button

			earlierAlert.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which){ dialog.dismiss();}
			});
			//Show Dialog
			AlertDialog dialog = earlierAlert.create();
			dialog.show();
		}
		//If user inputs correct answer
		else if(date.equals(bornEnt)) {
			numOfTickets = entity.getAwardedTicketNumber();
			currentTicketWon += numOfTickets;
			ticketsum.setText("The total ticket is:" + currentTicketWon);
			//Use Alert here to let user know they have won
			//Welcome Alert
			AlertDialog.Builder winAlert = new AlertDialog.Builder(GuessMaster.this);
			winAlert.setTitle("You Won");
			winAlert.setMessage("BINGO!" + entity.closingMessage());
			winAlert.setCancelable(false); // No Cancel Button

			winAlert.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which){Toast.makeText(getBaseContext(), "Awarded Ticket Number is:" + numOfTickets, Toast.LENGTH_SHORT).show();}
			});
			//Show Dialog
			AlertDialog dialog = winAlert.create();
			dialog.show();
			ContinueGame();
		}
		//If user inputs any other answers
		else{
			AlertDialog.Builder earlierAlert = new AlertDialog.Builder(GuessMaster.this);
			earlierAlert.setTitle("Incorrect!");
			earlierAlert.setMessage("Try Again!");
			earlierAlert.setCancelable(false); // No Cancel Button

			earlierAlert.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialop, int which){ dialop.dismiss();}
			});
			//Show Dialog
			AlertDialog dialog = earlierAlert.create();
			dialog.show();
		}
	}
	//This method generates a random number first, which will be used to randomly
	public void playGame() {
			int entityId = genRandomEntityId();
			playGame(entityId);
	}
	//Gets random entity
	public int genRandomEntityId() {
		Random randomNumber = new Random();
		return randomNumber.nextInt(numOfEntities);
	}
	//Method that deletes user inputs and changes entity
    public void changeEntity(){
		//Clears inputs
		userIn.setText(" ");
		//Find random entity
		final int randInd = genRandomEntityId();
		final Entity entity = entities[randInd];
		//set input entity and image
        entityName.setText(entity.getName());
        ImageSetter(entity);
        //Click button
        guessButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                playGame(entity);
            }
        });
    }
	//sets proper images
    public void ImageSetter(Entity entity) {
		if (entity.getName() == ("Justin Trudeau")) {
            entityImage.setImageResource(R.drawable.justint);
        } else if (entity.getName() == ("Celine Dion")) {
            entityImage.setImageResource(R.drawable.celidion);
        } else if (entity.getName() == ("United States")) {
            entityImage.setImageResource(R.drawable.usaflag);
        } else if (entity.getName() == ("myCreator")) {
            entityImage.setImageResource(R.drawable.download);
        }
	}
	//welcoming methods
	public void welcomeToGame(Entity entity){
    	//Welcome Alert
		AlertDialog.Builder welcomeAlert = new AlertDialog.Builder(GuessMaster.this);
			welcomeAlert.setTitle("GuessMaster Game v3");
			welcomeAlert.setMessage(entity.welcomeMessage());
			welcomeAlert.setCancelable(false); // No Cancel Button

		welcomeAlert.setNegativeButton("start Game", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which){Toast.makeText(getBaseContext(), "Game is Starting... Enjoy", Toast.LENGTH_SHORT).show();}
		});
		//Show Dialog
		AlertDialog dialog = welcomeAlert.create();
		dialog.show();
	}
	//Method that continues game
	public void ContinueGame(){
		//Clears user inputs
	    userIn.setText("");
	    //finds random entity
        final int randInd = genRandomEntityId();
        final Entity entity = entities[randInd];
        entityName.setText(entity.getName());
		//Call the ImageSetter method
		ImageSetter(entity);
		//Print the name of the entity to be guessed
		//in the entityName textview
        guessButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                playGame(entity);
            }
        });
		//Clear previous entry
		userIn.getText().clear();
	}
}

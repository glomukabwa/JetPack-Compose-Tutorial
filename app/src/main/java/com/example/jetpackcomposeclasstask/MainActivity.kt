package com.example.jetpackcomposeclasstask

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposeclasstask.ui.theme.JetpackComposeClassTaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeClassTaskTheme {/*This makes it apply the themes in Theme.kt. and enables us to use Material Design.
            See explanation in the comments inside the Image brackets in the MessageCard function*/
                Surface (modifier = Modifier.fillMaxSize()){/*A surface is like a Pane in FX. It's where things are placed. The fillMaxSize() makes it cover the whole screen, it's like saying 100vh and 100% width in css*/
                    /*
                    MessageCard(Message("Android", "Jetpack Compose"))
                    /*We call the function above in onCreate to display it when the app actually runs but we call it in @Preview below just to
                    display it when in the preview page here so don't be confused on why we are calling it twice*/
                    */
                    Conversation(SampleData.conversationSample)
                }
            }
        }
    }
}

data class Message(val author: String, val body: String)/*This is gonna be used in the function below to display the author's name and text*/
/*The reason that the variables in Message are constant(val) is so that every time a new message is created, a new object is created. So even if it is the same author, this is how it will look:
Message("Gloria", "Hello")
Message("Gloria", "How are you?")
Message("Gloria", "I’ll be late")
Compose prefers this so that it can be able to keep track of the changes. If we made the body a var, if Gloria
texted "How are you?" after "Hello", now in our memory, we'd only have "How are you?", the "Hello" would be lost.
We want to keep track of everything so that's why the variables are constant.
So plz note that in the function MesssageCard, everytime a new message is typed, a new object is created*/

/*Below, we put @Composable before the function to tell the compiler that the function I'm about to create should be treated as
a Jetpack compose function. Characteristics of a Jetpack compose function include:
*Idempotent: Composable functions must always produce the same output when run with the same parameter values.
*Declarative: Composable functions declare what the UI should look like, rather than how to update it.
*Recomposable: Composable functions can be recomposed (i.e., re-run) when their dependencies change.
* We do this cz there are other ways to create UI like using views which uses xml and I'm guessing things are done differently there*/
@Composable
fun MessageCard(msg: Message){/*So this function accepts an object of the type Message(the data class above).*/
    Row (modifier = Modifier.padding(all = 8.dp))/*This adds padding around the inside of the row*/{
        Image(
            painter = painterResource(R.drawable.profile_icon),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp) // Set image size to 40 dp cz the original one is so big
                .clip(CircleShape)// Clip the image to be shaped as a circle
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape) //This creates a border around the image and we are only able to see the primary color we've applied here cz we've surrounded the preview and onCreate function with the theme(JetPackComposeClassTaskTheme).
                //Material Theme is under Material Design and Material Design allows us to deal with color, typography(size) and shape(border corners). See use of typography in the msg.author and msg.body below.
        )

        Spacer(modifier = Modifier.width(8.dp))//Add horizontal space between the profile and the message

        // We keep track if the message is expanded or not in this variable
        var isExpanded by remember { mutableStateOf(false) }
        /*This line creates state inside your composable. What is “state” in Compose? A state is data that, when it changes, causes the
        UI to automatically recompose (redraw).
        mutableStateOf(false) -> This creates an observable mutable state whose initial value is false.
        Think of it like: A box that stores a value, Compose watches this box. When the value changes → UI automatically updates
        remember { ... } -> This makes Compose keep the value across recompositions. Without remember, every time Compose redraws the
        UI, isExpanded would reset to false.Example: If the user clicks the message and expands it, isExpanded becomes true,  UI
        recomposes. Without remember, the value would go back to false. You would never see it expand
        var isExpanded by ... -> The by is Kotlin’s property delegation, which lets you treat the state like a normal variable.Instead
         of writing:isExpanded.value = true , You can simply write: isExpanded = true. This makes the state easier to use.*/

        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )/*What does animateColorAsState() do? Normally, if you change a color from: Color.White → Color.Blue, the color switches
         instantly. But with: animateColorAsState(), Compose will animate the transition gradually:
         white → light blue → medium blue → dark blue → final blue. This makes the UI smoother.
         The input to animateColorAsState depends on isExpanded. So: When isExpanded == false → color is surface(meaning it retains the
         current surface colors) and when isExpanded == true → color becomes primary. Because it’s animated, it does not jump instantly,
         it transitions smoothly from one color to the other.
         So we can say simply that animateColorAsState receives an input which is the value of isExpanded then applies the if..else..
         statement then gets an output and that output is stored by the variable surfaceColor*/

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) /*This means that if the value was true, it becomes false and
         if false, it becomes true. It's used to trace if it has been clicked or not*/{
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary, //Check the actual color that secondary is in Theme.kt
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))//Add vertical space between the owner of the message and the actual message

            Surface(shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,/*You'll notice an oval greyish border around it.
                The medium defines how rounded you want ur corners to be. Small is less rounded and large is more rounded.I can't see the
                exact measurements of the shapes in Theme.kt so I'm guessing its using the default but you can customize if u want*/
                color = surfaceColor, // surfaceColor color will be changing gradually from primary to surface
                modifier = Modifier.animateContentSize().padding(1.dp)// animateContentSize will change the Surface size gradually
            ){
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,//This means that if the message has been clicked(so isExpanded = true), let it occupy the maximum amount of lines it can occupy, it hasn't been clicked, let it occupy 1 line
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    /*To preview this function, we have to use the @Preview annotation but it can't be used on a function that accepts
    parameters so we have to create another composable function ( PreviewMessage() below and then call this function*/
}


/*Below, we are using @Preview just to preview the function in the split screen so that we don't have to keep running the app to
see how it looks. This code is not used when actually running the app. It saves resources cz developers don't have to constantly
run the app to see the UI*/
@Preview(name = "LightMode")//This is gonna show the first preview which is Light Mode preview. name gives the preview a name, u'll notice it on top of the screen
@Preview(//This is gonna show a second preview which is the dark mode
    uiMode = Configuration.UI_MODE_NIGHT_YES,//We have to actively change it to dark mode colors or it will default to light mode colors
    showBackground = true,//We have to actively show the background of dark mode or it will default to false and become transparent so there will be no background(since my Android Studio is in dark mode, might not notice the difference without this but it is there)
    name = "Dark Mode"//The name of the 2nd preview
)

@Composable
fun PreviewMessage(){
    JetpackComposeClassTaskTheme {/*This makes it apply the themes in Theme.kt. U'll notice that before it was there, the preview had no color. Now it has a white background if it is light-mode(default) or a dark background if dark-mode is activated*/
        Surface (modifier = Modifier.fillMaxSize()){/*This is like a Pane in FX and the fillMaxSize() makes it occupy all the screen space of a mobile phone*/
            MessageCard(Message("Lexi", "Hey, take a look at Jetpack Compose"))
        }
    }
}

@Composable
fun Conversation(messages: List<Message>){
    /*You'll notice that in the brackets of the function we haven't written val messages, we've just written it on its own. So
    apparently, for function parameters you don't usually put var or val. This is because function parameters can't change what they
    are referring to. For example, if our parameter was messages: String, inside the function we can't change that parameter, we can't
    reassign it, we can only use it so it is behaving like a val. You can't do anything to a val once it is declared. Kotlin therefore
    doesn't make you write that its a val cz that's redundant. You also can't write var cz that would be wrong. It doesn't behave like
    a var.
    Within the brackets of List, we are usually supposed to put the type of List it is. It could be a string, int , object etc.
    In this case, it's an object of the data class Message that we created before so we specify it here.*/
    LazyColumn {
        /*A LazyColumn is like a vertical RecyclerView. A RecyclerView is designed to load and display only the items that are currently
         visible on the screen, plus a few extra that are just off-screen to prepare for smooth scrolling. When an item scrolls
         off-screen, its ViewHolder is reused for the next item coming into view.*/
        items(messages) { message ->
            MessageCard(message)
        }/*What this part of the code does is that it says that for each message in messages, call MessageCard(message). So call
        MessageCard and MessageCard usually needs an argument that is the message being passed so pass one message from the list
        we have. Then it also tells LazyColumn, display the output of MessageCard and LazyColumn does it in a vertical RecyclerView*/
    }
}

@Preview
@Composable
fun previewConversation(){
    JetpackComposeClassTaskTheme {
        Surface {/*I don't  need to put fillMaxSize() here cz the messages are so many so they will definitely surpass the screen size.
         With the other one, I had to do that or it would only be the size of one message*/
            Conversation(SampleData.conversationSample)
        }
    }
}
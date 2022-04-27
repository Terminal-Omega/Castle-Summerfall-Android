package com.example.textadventure

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.textadventure.ui.theme.TextAdventureTheme
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val player = Player(0, 0, 5, 20, 2)//TODO: make these mutable states
        val floorSize = 5
        val floor = Generator.generateFloor(floorSize, floorSize)
        setContent {
            TextAdventureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyApp(player = player, floor = floor, floorSize = floorSize)
                }
            }
        }
    }
}

@Composable
fun BottomBar(text: MutableState<String>, player: Player, floorSize: Int, floor: Floor) {
    Row(modifier = Modifier.padding(horizontal= 10.dp, vertical = 20.dp)) {
        val textState = remember { mutableStateOf("") }
        TextField(
            value = textState.value,
            onValueChange = { textState.value = it; },
            singleLine = true,
            placeholder = @Composable { Text(text = "Enter Command Here") }
        )
        Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        Button(
            onClick = {
                text.value = runCommand(textState.value, player = player, floorSize = floorSize, floor = floor);
                textState.value = ""
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
        ) {
            Text(text = "Enter", modifier = Modifier.padding(10.dp))
        }
    }
}

@Composable
fun TopBar(menuFun: () -> Unit){
    TopAppBar {
        Icon(painter = painterResource(id = R.drawable.menu), contentDescription = null, modifier = Modifier.clickable {menuFun()})
        Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        Text("Topbar")
    }
}

@Composable
fun TextBox(text: MutableState<String>){
    Text(text = text.value, fontSize = 32.sp)
    Box(){

    }
}

@Composable
fun Game(menuFun: () -> Unit, floor: Floor, player: Player, floorSize: Int){
    val text = remember { mutableStateOf("Welcome to my Game") }
    Scaffold(
        topBar = {TopBar(menuFun = menuFun)}
    ) {
        Column(modifier = Modifier.verticalScroll(ScrollState(1))) {
            TextBox(text = text)
            BottomBar(text, floor = floor, player = player, floorSize = floorSize)
        }
        }
    }

@Composable
fun MyApp(floorSize: Int, floor: Floor, player: Player){
    var menuScreenShow = remember { mutableStateOf(false) }

    if (menuScreenShow.value){
        menuScreen(onClicked = {menuScreenShow.value = false})
    }else{
        Game(menuFun = {menuScreenShow.value = true; Log.d("Clicked", "This has been clicked")}, player = player, floorSize = floorSize, floor = floor)
    }
}

@Composable
fun menuScreen(onClicked: () -> Unit){
    Box(modifier = Modifier.size(20.dp)) {
        Button(onClick = onClicked) {
            Text("Back")
        }
    }
}

    

fun runCommand(inputCommand: String, floor: Floor, player: Player, floorSize: Int): String {
    var outputText = ""
    val helpPat: Pattern = Pattern.compile("[Hh]elp ([a-z].*)")
    val movePat: Pattern = Pattern.compile("[Mm]ove ([N|n|S|s|W|w|E|e])")
    val inspectPat: Pattern = Pattern.compile("[Ii]nspect ([A-Za-z].*)")
    val takePat: Pattern = Pattern.compile("[tT]ake ([A-Za-z].*)")
    val bookPat: Pattern = Pattern.compile("[Bb]ookmark ([A-Za-z].*?) : ([A-Za-z].*)")
    val dropPat: Pattern = Pattern.compile("[Dd]rop ([A-Za-z].*)")
    val attackPat: Pattern = Pattern.compile("[Aa]ttack ([A-Za-z].*?) [Ww].* ([A-Za-z].*)")

    val helpMatch: Matcher = helpPat.matcher(inputCommand)
    val moveMatch: Matcher = movePat.matcher(inputCommand)
    val inspectMatch: Matcher = inspectPat.matcher(inputCommand)
    val takeMatch: Matcher = takePat.matcher(inputCommand)
    val dropMatch: Matcher = dropPat.matcher(inputCommand)
    val attackMatch: Matcher = attackPat.matcher(inputCommand)
    val bookMatch: Matcher = bookPat.matcher(inputCommand)

    if (inputCommand == UI.Commands.LOOK_AROUND.strCommand){
        outputText = floor.getDescription(player.getXCoord(), player.getYCoord())

    }

    if (helpMatch.find()){
        outputText = UI.helpCommand(helpMatch.group(1))
    }

    if (moveMatch.find()){
        outputText = UI.move(moveMatch.group(1), player, floor, floorSize)
    }

    if (takeMatch.find()){
        try {
            val interactable: Interactable = floor.getRoom(player.getXCoord(), player.getYCoord()).takeItem(takeMatch.group(1))
            if (player.addItem(interactable)){
                outputText = "You put the " + takeMatch.group(1) + " in your bag"
            }
        }catch (e: ThingNotFoundException){
            outputText = e.toString()
        }
    }

    if (inspectMatch.find()){
        val interactable: Interactable = floor.getRoom(player.getXCoord(), player.getYCoord()).getItem(inspectMatch.group(1))
        outputText = interactable.getDescription()
    }



    if (outputText == ""){
        outputText = "Sorry I don't know what command you wanted"
    }

    return outputText;
}

//@Preview(showBackground = true, widthDp = 300, heightDp = 500)
//@Composable
//fun DefaultPreview() {
//    TextAdventureTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colors.background
//        ) {
//            Game()
//        }
//    }
//}
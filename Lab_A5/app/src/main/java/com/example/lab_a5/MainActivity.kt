package com.example.lab_a5

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val filmViewModel: FilmsViewModel = viewModel()
            GenerateList(filmViewModel)
            val modifier: Modifier = Modifier
            HomeScreen(modifier, filmViewModel)
        }
    }
}

data class FilmsData(val name : String, val year : String, val ranking : Double)


class FilmsViewModel : ViewModel() {
    val _films = mutableStateListOf<FilmsData>()

    fun sortFilms(){
        _films.sortByDescending { film -> film.ranking }
    }

    fun sortAscending(){
        _films.sortBy { film -> film.ranking }
    }

    fun addFilm(name: String, year: String, ranking : Double) {
        _films.add(FilmsData(name, year,  ranking))
        sortFilms()
    }

    fun deleteFilm(film: FilmsData){
        _films.remove(film)
    }

    fun updateFilmYear(film: FilmsData, newYear: String){
        val index = _films.indexOf(film)
        if(index != -1){
            if(newYear.isNotEmpty()){ _films[index] = _films[index].copy(year = newYear) }
            sortFilms()
        }
    }

    fun updateFilmRating(film: FilmsData, newRanking: Double){
        val index = _films.indexOf(film)
        if(index != -1){
            if(newRanking != -1.0){ _films[index] = _films[index].copy(ranking = newRanking) }
            sortFilms()
        }
    }
}

@Composable
fun GenerateList(filmViewModel: FilmsViewModel = viewModel()){
    if (filmViewModel._films.isEmpty()) {
        filmViewModel.addFilm("The Green Mile", "1999",8.6)
        filmViewModel.addFilm("The Godfather ", "1972",8.59)
        filmViewModel.addFilm("The Shawshank Redemption", "1994", 8.79)
        filmViewModel.addFilm("Intouchables", "2011", 8.65)
        filmViewModel.addFilm("Forrest Gump", "1994", 8.5)
        filmViewModel.addFilm("Avatar", "2009", 7.5)
        filmViewModel.addFilm("Shrek", "2001", 7.8)
        filmViewModel.addFilm("Doctor Strange", "2016", 7.3)
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier,
               filmViewModel: FilmsViewModel = viewModel()) {
    val showPopup by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Lab A5 - LIST OF FILMS RANKING")
        Spacer(Modifier.height(20.dp))
        InputData(modifier = modifier, filmViewModel = filmViewModel, remember { mutableStateOf(showPopup) })
    }
}

@Composable
fun InputData(modifier: Modifier = Modifier,
              filmViewModel: FilmsViewModel = viewModel(),
              showPopup: MutableState<Boolean>){
    var ascending by rememberSaveable { mutableStateOf(false) }
    if(!showPopup.value){
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                modifier = modifier.wrapContentWidth(),
                onClick = {
                    showPopup.value = true
                }
            ) {
                Text("ADD FILM")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                modifier = modifier.wrapContentWidth(),
                onClick = {
                    ascending = !ascending
                    if(ascending){
                        filmViewModel.sortAscending()
                    }
                    else{
                        filmViewModel.sortFilms()
                    }
                }
            ) {
                if(ascending){

                    Text("Sort Desceding")
                }
                else{
                    Text("Sort Ascending")
                }
            }

        }
    }
    else {
        ShowAddFilmDialog(filmViewModel, showPopup)
    }

    Spacer(Modifier.height(20.dp))

    val showOptions by rememberSaveable { mutableStateOf(false) }

    DirectionsList( filmViewModel = filmViewModel,
                    showOption = remember { mutableStateOf(showOptions) }
    )
}

@Composable
fun ShowAddFilmDialog(filmViewModel: FilmsViewModel,
                      showPopup: MutableState<Boolean>){
    var name by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var ranking by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val currentDate = LocalDate.now()

    AlertDialog(
        onDismissRequest = {
        },
        title = { Text(text = "Add movie") },
        text = {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(IntrinsicSize.Min)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = name,
                        onValueChange = { newValue ->
                            name = newValue
                        },
                        label = { Text("Film name: ") },
                        modifier = Modifier.fillMaxWidth()
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    TextField(
                        value = year,
                        onValueChange = { newValue ->
                            year = newValue
                        },
                        label = { Text("Year: ")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    TextField(
                        value = ranking,
                        onValueChange = { newValue ->
                            ranking = newValue.replace(",", ".")
                        },
                        label = {Text("Ranking")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if(name.isNotEmpty() and year.isNotBlank() and ranking.isNotEmpty()){
                        try{
                            val enteredYear = year.toInt()
                            if (enteredYear > currentDate.year || enteredYear < 0) {
                                Toast.makeText(context, "Year must be a valid positive number and cannot be in the future", Toast.LENGTH_SHORT).show()
                            }
                            else if(ranking.toDouble() !in 0.0..10.0){
                                Toast.makeText(context,"Ranking must be between 0.0 and 10.0", Toast.LENGTH_SHORT).show()
                            }
                            else if( filmViewModel._films.find { film -> (film.name == name) and (film.year == year)} == null ){
                                filmViewModel.addFilm(name, year, ranking.toDouble())
                                name = ""
                                year = ""
                                ranking = ""
                                showPopup.value = false
                            }
                            else{
                                Toast.makeText(context,"Film with the same name exists in the list", Toast.LENGTH_SHORT).show()
                            }
                        }
                        catch (e: NumberFormatException) {
                            Toast.makeText(context, "Year must be a number", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(context,"Please input all data", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ){
                Text("Add movie",
                    modifier = Modifier.padding(10.dp),
                    color = Color.Green)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    showPopup.value = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ){
                Text("Cancel",
                    modifier = Modifier.padding(10.dp),
                    color = Color.Red)
            }
        }
    )
}

@Composable
fun DirectionsList(filmViewModel: FilmsViewModel,
                   showOption: MutableState<Boolean>) {
    val showUpdate by rememberSaveable { mutableStateOf(false) }
    val films = filmViewModel._films
    LazyColumn {
        items(films) { film ->
            FilmItem(film, filmViewModel, showOption, remember { mutableStateOf(showUpdate) } )
        }
    }

}

@Composable
fun FilmItem(film: FilmsData,
             filmViewModel: FilmsViewModel,
             showOption: MutableState<Boolean>,
             showUpdate: MutableState<Boolean>) {
    var fullItem by rememberSaveable { mutableStateOf(false) }
    var modifierRow = Modifier.fillMaxWidth()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        fullItem = !fullItem
                    },
                    onLongPress = {
                        showOption.value = !showOption.value
                    }
                )
            },
        horizontalArrangement = Arrangement.Start
    ) {
        Column{
            if(showOption.value){
                modifierRow = Modifier.wrapContentSize()
            }
            Row(
                modifier = modifierRow,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column{
                    Text(film.name,
                        modifier = Modifier.padding(10.dp),
                        color = Color.Black)
                }
                if(!fullItem and !showOption.value){
                    Column(){
                        Text("Ranking: ${film.ranking}",
                            modifier = Modifier.padding(10.dp),
                            color = Color.Black)
                    }
                }
            }
            if(fullItem){
                Row{
                    Column{
                        Text("Year: ${film.year}",
                            modifier = Modifier.padding(10.dp),
                            color = Color.Black)
                    }
                    Column(){
                        Text("Ranking: ${film.ranking}",
                            modifier = Modifier.padding(10.dp),
                            color = Color.Black)
                    }
                }
            }
        }

        if(showOption.value) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(IntrinsicSize.Min)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    Button(
                        modifier = Modifier.wrapContentSize(),
                        onClick = {
                            showUpdate.value = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBB86FC))
                    ) {
                        Text("Update",
                            modifier = Modifier.padding(1.dp),
                            color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    Button(
                        modifier = Modifier.wrapContentSize(),
                        onClick = {
                            filmViewModel.deleteFilm(film)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9967A))
                    ) {
                        Text("Delete",
                            modifier = Modifier.padding(1.dp),
                            color = Color.Black)
                    }
                }
            }
            if(showUpdate.value){
                ShowUpdateDialog(filmViewModel, showUpdate, film)
            }
        }
    }
    Spacer(modifier = Modifier.height(2.dp))
}

@Composable
fun ShowUpdateDialog(filmViewModel: FilmsViewModel,
                     showUpdate: MutableState<Boolean>,
                     filmsData: FilmsData){
    var newYear by rememberSaveable { mutableStateOf("") }
    var newRanking by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val currentDate = LocalDate.now()
    var update by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {
        },
        title = { Text(text = "Change movie information") },
        text = {
            Column(){
                TextField(
                    value = newYear,
                    onValueChange = { newValue ->
                        newYear = newValue
                    },
                    label = { Text("New year") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = newRanking,
                    onValueChange = { newValue ->
                        newRanking = newValue.replace(",", ".")
                    },
                    label = { Text("New ranking") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    update = false
                    if( newYear.isNotBlank() or newYear.isNotEmpty()){
                        try {
                            val enteredYear = newYear.toInt()
                            if (enteredYear > currentDate.year || enteredYear < 0) {
                                Toast.makeText(context,"Year must be a valid positive number and cannot be in the future",Toast.LENGTH_SHORT).show()
                            }
                            else{
                                filmViewModel.updateFilmYear(filmsData, newYear)
                                newYear = ""
                                update = true
                            }
                        }
                        catch (e: NumberFormatException) {
                            Toast.makeText(context, "Year must be a number", Toast.LENGTH_SHORT).show()
                        }
                    }
                    if(newRanking.isNotEmpty()){
                        if(newRanking.toDouble() !in 0.0..10.0){
                            Toast.makeText(context,"Ranking must be between 0.0 and 10.0", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            filmViewModel.updateFilmRating(filmsData, newRanking.toDouble())
                            newRanking = ""
                            update = true
                        }
                    }
                    if(update){
                        showUpdate.value = false
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ){
                Text("Update",
                    modifier = Modifier.padding(10.dp),
                    color = Color.Green)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    showUpdate.value = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ){
                Text("Cancel",
                    modifier = Modifier.padding(10.dp),
                    color = Color.Red)
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HomeScreen()
}
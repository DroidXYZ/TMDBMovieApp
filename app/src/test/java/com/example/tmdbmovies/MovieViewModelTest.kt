package com.example.tmdbmovies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.tmdbmovies.BuildConfig.API_KEY
import com.example.tmdbmovies.models.moviedetail.MovieDetailResponse
import com.example.tmdbmovies.models.movielist.MovieResponse
import com.example.tmdbmovies.tmdbutils.TMDBConstants
import com.example.tmdbmovies.ui.movies.MovieRepository
import com.example.tmdbmovies.ui.movies.MovieRepositoryInterface
import com.example.tmdbmovies.ui.movies.MoviesViewModel
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = CoroutineRule()

//    // Subject under test
//    private lateinit var viewModel: MoviesViewModel

    @Mock
    private lateinit var repository: MovieRepository

    @Mock
    private lateinit var repositoryInterface: MovieRepositoryInterface

    @Mock
    private lateinit var apiMovieListObserver: Observer<MovieResponse>

    @Mock
    private lateinit var apiMovieDetailObserver: Observer<MovieDetailResponse>

    @Mock
    private lateinit var apiErrorObserver: Observer<String>

    @Before
    fun setup() {
    }

//       val movieResponseString = "{\"page\":1,\"results\":[{\"adult\":false,\"backdrop_path\":\"/iQFcwSGbZXMkeyKrxbPnwnRo5fl.jpg\",\"genre_ids\":[28,12,878],\"id\":634649,\"original_language\":\"en\",\"original_title\":\"Spider-Man: No Way Home\",\"overview\":\"Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a super-hero. When he asks for help from Doctor Strange the stakes become even more dangerous, forcing him to discover what it truly means to be Spider-Man.\",\"popularity\":5786.583,\"poster_path\":\"/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg\",\"release_date\":\"2021-12-15\",\"title\":\"Spider-Man: No Way Home\",\"video\":false,\"vote_average\":8.2,\"vote_count\":11158},{\"adult\":false,\"backdrop_path\":\"/x747ZvF0CcYYTTpPRCoUrxA2cYy.jpg\",\"genre_ids\":[28,12,878],\"id\":406759,\"original_language\":\"en\",\"original_title\":\"Moonfall\",\"overview\":\"A mysterious force knocks the moon from its orbit around Earth and sends it hurtling on a collision course with life as we know it.\",\"popularity\":5685.758,\"poster_path\":\"/odVv1sqVs0KxBXiA8bhIBlPgalx.jpg\",\"release_date\":\"2022-02-03\",\"title\":\"Moonfall\",\"video\":false,\"vote_average\":6.5,\"vote_count\":575},{\"adult\":false,\"backdrop_path\":\"/fOy2Jurz9k6RnJnMUMRDAgBwru2.jpg\",\"genre_ids\":[16,10751,35,14],\"id\":508947,\"original_language\":\"en\",\"original_title\":\"Turning Red\",\"overview\":\"Thirteen-year-old Mei is experiencing the awkwardness of being a teenager with a twist – when she gets too excited, she transforms into a giant red panda.\",\"popularity\":5038.067,\"poster_path\":\"/qsdjk9oAKSQMWs0Vt5Pyfh6O4GZ.jpg\",\"release_date\":\"2022-03-01\",\"title\":\"Turning Red\",\"video\":false,\"vote_average\":7.4,\"vote_count\":1487},{\"adult\":false,\"backdrop_path\":\"/egoyMDLqCxzjnSrWOz50uLlJWmD.jpg\",\"genre_ids\":[28,878,35,10751],\"id\":675353,\"original_language\":\"en\",\"original_title\":\"Sonic the Hedgehog 2\",\"overview\":\"After settling in Green Hills, Sonic is eager to prove he has what it takes to be a true hero. His test comes when Dr. Robotnik returns, this time with a new partner, Knuckles, in search for an emerald that has the power to destroy civilizations. Sonic teams up with his own sidekick, Tails, and together they embark on a globe-trotting journey to find the emerald before it falls into the wrong hands.\",\"popularity\":6587.056,\"poster_path\":\"/1j6JtMRAhdO3RaXRtiWdPL5D3SW.jpg\",\"release_date\":\"2022-03-30\",\"title\":\"Sonic the Hedgehog 2\",\"video\":false,\"vote_average\":7.7,\"vote_count\":197},{\"adult\":false,\"backdrop_path\":\"/ewUqXnwiRLhgmGhuksOdLgh49Ch.jpg\",\"genre_ids\":[28,12,35,878],\"id\":696806,\"original_language\":\"en\",\"original_title\":\"The Adam Project\",\"overview\":\"After accidentally crash-landing in 2022, time-traveling fighter pilot Adam Reed teams up with his 12-year-old self on a mission to save the future.\",\"popularity\":1978.467,\"poster_path\":\"/wFjboE0aFZNbVOF05fzrka9Fqyx.jpg\",\"release_date\":\"2022-03-11\",\"title\":\"The Adam Project\",\"video\":false,\"vote_average\":7,\"vote_count\":1611},{\"adult\":false,\"backdrop_path\":\"/t7I942V5U1Ggn6OevN75u3sNYH9.jpg\",\"genre_ids\":[28,53],\"id\":760868,\"original_language\":\"sv\",\"original_title\":\"Svart krabba\",\"overview\":\"To end an apocalyptic war and save her daughter, a reluctant soldier embarks on a desperate mission to cross a frozen sea carrying a top-secret cargo.\",\"popularity\":1887.864,\"poster_path\":\"/mcIYHZYwUbvhvUt8Lb5nENJ7AlX.jpg\",\"release_date\":\"2022-03-18\",\"title\":\"Black Crab\",\"video\":false,\"vote_average\":6.2,\"vote_count\":286},{\"adult\":false,\"backdrop_path\":\"/3G1Q5xF40HkUBJXxt2DQgQzKTp5.jpg\",\"genre_ids\":[16,35,10751,14],\"id\":568124,\"original_language\":\"en\",\"original_title\":\"Encanto\",\"overview\":\"The tale of an extraordinary family, the Madrigals, who live hidden in the mountains of Colombia, in a magical house, in a vibrant town, in a wondrous, charmed place called an Encanto. The magic of the Encanto has blessed every child in the family with a unique gift from super strength to the power to heal—every child except one, Mirabel. But when she discovers that the magic surrounding the Encanto is in danger, Mirabel decides that she, the only ordinary Madrigal, might just be her exceptional family's last hope.\",\"popularity\":1964.147,\"poster_path\":\"/4j0PNHkMr5ax3IA8tjtxcmPU3QT.jpg\",\"release_date\":\"2021-11-24\",\"title\":\"Encanto\",\"video\":false,\"vote_average\":7.7,\"vote_count\":5892},{\"adult\":false,\"backdrop_path\":\"/5P8SmMzSNYikXpxil6BYzJ16611.jpg\",\"genre_ids\":[80,9648,53],\"id\":414906,\"original_language\":\"en\",\"original_title\":\"The Batman\",\"overview\":\"In his second year of fighting crime, Batman uncovers corruption in Gotham City that connects to his own family while facing a serial killer known as the Riddler.\",\"popularity\":1887.324,\"poster_path\":\"/74xTEgt7R36Fpooo50r9T25onhq.jpg\",\"release_date\":\"2022-03-01\",\"title\":\"The Batman\",\"video\":false,\"vote_average\":7.9,\"vote_count\":2727},{\"adult\":false,\"backdrop_path\":\"/2hGjmgZrS1nlsEl5PZorn7EsmzH.jpg\",\"genre_ids\":[28,53],\"id\":823625,\"original_language\":\"en\",\"original_title\":\"Blacklight\",\"overview\":\"Travis Block is a shadowy Government agent who specializes in removing operatives whose covers have been exposed. He then has to uncover a deadly conspiracy within his own ranks that reaches the highest echelons of power.\",\"popularity\":1722.196,\"poster_path\":\"/bv9dy8mnwftdY2j6gG39gCfSFpV.jpg\",\"release_date\":\"2022-02-10\",\"title\":\"Blacklight\",\"video\":false,\"vote_average\":6.1,\"vote_count\":250},{\"adult\":false,\"backdrop_path\":\"/33wnBK5NxvuKQv0Cxo3wMv0eR7F.jpg\",\"genre_ids\":[27,53],\"id\":833425,\"original_language\":\"en\",\"original_title\":\"No Exit\",\"overview\":\"Stranded at a rest stop in the mountains during a blizzard, a recovering addict discovers a kidnapped child hidden in a car belonging to one of the people inside the building which sets her on a terrifying struggle to identify who among them is the kidnapper.\",\"popularity\":1524.947,\"poster_path\":\"/5cnLoWq9o5tuLe1Zq4BTX4LwZ2B.jpg\",\"release_date\":\"2022-02-25\",\"title\":\"No Exit\",\"video\":false,\"vote_average\":6.6,\"vote_count\":319},{\"adult\":false,\"backdrop_path\":\"/An1nKjXahrChfEbZ3MAE8fsiut1.jpg\",\"genre_ids\":[27],\"id\":661791,\"original_language\":\"es\",\"original_title\":\"La abuela\",\"overview\":\"A Paris model must return to Madrid where her grandmother, who raised her, has had a stroke. But spending just a few days with this relative turns into an unexpected nightmare.\",\"popularity\":1504.3,\"poster_path\":\"/19rA9FjhwI4VEfaCXV7648XUInR.jpg\",\"release_date\":\"2022-01-28\",\"title\":\"The Grandmother\",\"video\":false,\"vote_average\":6.3,\"vote_count\":71},{\"adult\":false,\"backdrop_path\":\"/tq3klWQevRK0Or0cGhsw0h3FDWQ.jpg\",\"genre_ids\":[12,16,35,10751,14],\"id\":676705,\"original_language\":\"fr\",\"original_title\":\"Pil\",\"overview\":\"Pil, a little vagabond girl, lives on the streets of the medieval city of Roc-en-Brume, along with her three tame weasels. She survives of food stolen from the castle of the sinister Regent Tristain. One day, to escape his guards, Pil disguises herself as a princess. Thus she embarks upon a mad, delirious adventure, together with Crobar, a big clumsy guard who thinks she's a noble, and Rigolin, a young crackpot jester. Pil is going to have to save Roland, rightful heir to the throne under the curse of a spell. This adventure will turn the entire kingdom upside down, and teach Pil that nobility can be found in all of us.\",\"popularity\":1627.835,\"poster_path\":\"/abPQVYyNfVuGoFUfGVhlNecu0QG.jpg\",\"release_date\":\"2021-08-11\",\"title\":\"Pil's Adventures\",\"video\":false,\"vote_average\":6.6,\"vote_count\":42},{\"adult\":false,\"backdrop_path\":\"/dK12GIdhGP6NPGFssK2Fh265jyr.jpg\",\"genre_ids\":[28,35,80,53],\"id\":512195,\"original_language\":\"en\",\"original_title\":\"Red Notice\",\"overview\":\"An Interpol-issued Red Notice is a global alert to hunt and capture the world's most wanted. But when a daring heist brings together the FBI's top profiler and two rival criminals, there's no telling what will happen.\",\"popularity\":1511.465,\"poster_path\":\"/lAXONuqg41NwUMuzMiFvicDET9Y.jpg\",\"release_date\":\"2021-11-04\",\"title\":\"Red Notice\",\"video\":false,\"vote_average\":6.8,\"vote_count\":3417},{\"adult\":false,\"backdrop_path\":\"/j0xO6355h5HfvC425sWDT6tiBZM.jpg\",\"genre_ids\":[28,12,14],\"id\":939243,\"original_language\":\"en\",\"original_title\":\"Sonic the Hedgehog 3\",\"overview\":\"The third film in the \\\"Sonic the Hedgehog\\\" franchise.\",\"popularity\":2076.914,\"poster_path\":\"/aNSBaYTgPz8QEADi3xiD52X4uVF.jpg\",\"release_date\":\"\",\"title\":\"Sonic the Hedgehog 3\",\"video\":false,\"vote_average\":0,\"vote_count\":0},{\"adult\":false,\"backdrop_path\":\"/stmYfCUGd8Iy6kAMBr6AmWqx8Bq.jpg\",\"genre_ids\":[28,878,35,10751],\"id\":454626,\"original_language\":\"en\",\"original_title\":\"Sonic the Hedgehog\",\"overview\":\"Powered with incredible speed, Sonic The Hedgehog embraces his new home on Earth. That is, until Sonic sparks the attention of super-uncool evil genius Dr. Robotnik. Now it’s super-villain vs. super-sonic in an all-out race across the globe to stop Robotnik from using Sonic’s unique power for world domination.\",\"popularity\":1992.634,\"poster_path\":\"/aQvJ5WPzZgYVDrxLX4R6cLJCEaQ.jpg\",\"release_date\":\"2020-02-12\",\"title\":\"Sonic the Hedgehog\",\"video\":false,\"vote_average\":7.4,\"vote_count\":7680},{\"adult\":false,\"backdrop_path\":\"/yzH5zvuEzzsHLZnn0jwYoPf0CMT.jpg\",\"genre_ids\":[53,28],\"id\":760926,\"original_language\":\"en\",\"original_title\":\"Gold\",\"overview\":\"In the not-too-distant future, two drifters traveling through the desert stumble across the biggest gold nugget ever found and the dream of immense wealth and greed takes hold. They hatch a plan to excavate their bounty, with one man leaving to secure the necessary tools while the other remains with the gold. The man who remains must endure harsh desert elements, ravenous wild dogs, and mysterious intruders, while battling the sinking suspicion that he has been abandoned to his fate.\",\"popularity\":1228.945,\"poster_path\":\"/ejXBuNLvK4kZ7YcqeKqUWnCxdJq.jpg\",\"release_date\":\"2022-01-13\",\"title\":\"Gold\",\"video\":false,\"vote_average\":6.6,\"vote_count\":168},{\"adult\":false,\"backdrop_path\":\"/tj4lzGgHrfjnjVqAKkLIjFqPSyO.jpg\",\"genre_ids\":[28,878,14],\"id\":526896,\"original_language\":\"en\",\"original_title\":\"Morbius\",\"overview\":\"Dangerously ill with a rare blood disorder, and determined to save others suffering his same fate, Dr. Michael Morbius attempts a desperate gamble. What at first appears to be a radical success soon reveals itself to be a remedy potentially worse than the disease.\",\"popularity\":1310.201,\"poster_path\":\"/7gmOjg7lQXGLW8wX31ry1IdIY07.jpg\",\"release_date\":\"2022-03-30\",\"title\":\"Morbius\",\"video\":false,\"vote_average\":5.9,\"vote_count\":329},{\"adult\":false,\"backdrop_path\":\"/i5dUmY2xRzgkmjHJYKNj8kPX1Xx.jpg\",\"genre_ids\":[37,28],\"id\":928999,\"original_language\":\"en\",\"original_title\":\"Desperate Riders\",\"overview\":\"After Kansas Red rescues young Billy from a card-game shootout, the boy asks Red for help protecting his family from the outlaw Thorn, who’s just kidnapped Billy’s mother, Carol. As Red and Billy ride off to rescue Carol, they run into beautiful, tough-as-nails Leslie, who’s managed to escape Thorn’s men. The three race to stop Thorn’s wedding to Carol with guns a-blazing - but does she want to be rescued?\",\"popularity\":989.18,\"poster_path\":\"/7pYYGm1dWZGkbJuhcuaHD6nE6k7.jpg\",\"release_date\":\"2022-02-25\",\"title\":\"Desperate Riders\",\"video\":false,\"vote_average\":6,\"vote_count\":27},{\"adult\":false,\"backdrop_path\":\"/eG0oOQVsniPAuecPzDD1B1gnYWy.jpg\",\"genre_ids\":[16,35,12,10751],\"id\":774825,\"original_language\":\"en\",\"original_title\":\"The Ice Age Adventures of Buck Wild\",\"overview\":\"The fearless one-eyed weasel Buck teams up with mischievous possum brothers Crash & Eddie as they head off on a new adventure into Buck's home: The Dinosaur World.\",\"popularity\":1172.102,\"poster_path\":\"/zzXFM4FKDG7l1ufrAkwQYv2xvnh.jpg\",\"release_date\":\"2022-01-28\",\"title\":\"The Ice Age Adventures of Buck Wild\",\"video\":false,\"vote_average\":6.9,\"vote_count\":950},{\"adult\":false,\"backdrop_path\":\"/mruT954ve6P1zquaRs6XG0hA5k9.jpg\",\"genre_ids\":[53],\"id\":800510,\"original_language\":\"en\",\"original_title\":\"Kimi\",\"overview\":\"A tech worker with agoraphobia discovers recorded evidence of a violent crime but is met with resistance when she tries to report it. Seeking justice, she must do the thing she fears the most: she must leave her apartment.\",\"popularity\":1074.72,\"poster_path\":\"/okNgwtxIWzGsNlR3GsOS0i0Qgbn.jpg\",\"release_date\":\"2022-02-10\",\"title\":\"Kimi\",\"video\":false,\"vote_average\":6.2,\"vote_count\":321}],\"total_pages\":33058,\"total_results\":661156}"
//        val movieResponse = Gson().fromJson(movieResponseString,MovieResponse::class.java)
    @Test
    fun fetchMovieListAndPosterImageURL() =
        mainCoroutineRule.runBlockingTest {
            //Given
            val movieResponse = mock(MovieResponse::class.java)
            val viewModel = MoviesViewModel(repository)
            viewModel.movieList.observeForever(apiMovieListObserver)
            viewModel.getMoviesAndConfiguration(API_KEY,TMDBConstants.APP_LANGUAGE,1)
            viewModel.movieList.postValue(movieResponse)

            //When
            doReturn(movieResponse)
                .`when`(repository).getMovieList(API_KEY,TMDBConstants.APP_LANGUAGE,1)

            //Than
            verify(repository).getMovieList(API_KEY,TMDBConstants.APP_LANGUAGE,1)
            verify(repository).getImageConfiguration(API_KEY)
            verify(apiMovieListObserver).onChanged(movieResponse)

            viewModel.movieList.removeObserver(apiMovieListObserver)
        }

    @Test
    fun fetchMovieDetail() =
        mainCoroutineRule.runBlockingTest {
            //Given
            val movieDetailResponse = mock(MovieDetailResponse::class.java)
            val viewModel = MoviesViewModel(repository)
            viewModel.movieDetail.observeForever(apiMovieDetailObserver)
            viewModel.getMoviesDetail(53455,API_KEY,TMDBConstants.APP_LANGUAGE)
            viewModel.movieDetail.postValue(movieDetailResponse)

            //When
            doReturn(movieDetailResponse)
                .`when`(repository).getMovieDetail(53455,API_KEY,TMDBConstants.APP_LANGUAGE)

            //Than
            verify(repository).getMovieDetail(53455,API_KEY,TMDBConstants.APP_LANGUAGE)
            verify(apiMovieDetailObserver).onChanged(movieDetailResponse)
            viewModel.movieDetail.removeObserver(apiMovieDetailObserver)
        }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        mainCoroutineRule.runBlockingTest {
            //Given
            val errorMessage = "Error Message"
            val viewModel = MoviesViewModel(repository)
            viewModel.errorMessage.observeForever(apiErrorObserver)
            viewModel.getMoviesAndConfiguration(API_KEY,TMDBConstants.APP_LANGUAGE,1)
            viewModel.errorMessage.postValue(errorMessage)

            //When
            doThrow(RuntimeException(errorMessage))
                .`when`(repository)
                .getMovieList(API_KEY,TMDBConstants.APP_LANGUAGE,1)

            //Than
            verify(repository).getImageConfiguration(API_KEY)
            verify(repository).getMovieList(API_KEY,TMDBConstants.APP_LANGUAGE,1)
            verify(apiErrorObserver).onChanged(RuntimeException(errorMessage).toString())
            viewModel.errorMessage.removeObserver(apiErrorObserver)
        }
    }
}

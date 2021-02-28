package com.example.epicture_compose.data.mock

import com.example.epicture_compose.data.model.*
import java.net.URL
import java.time.LocalDate

// User

val userMock1 = User(
        name = "Magalie",
        pictureURL = "https://gravatar.com/avatar/7f52f730a43da991573ffc0956d766c6?s=400&d=robohash&r=x",
)
val userMock2 = User(
        name = "Pierre",
        pictureURL = "https://gravatar.com/avatar/4202cd1dedd668355d9d51ac58c9d8e5?s=400&d=robohash&r=x",
)
val userMock3 = User(
        name = "Jean",
        pictureURL = "https://gravatar.com/avatar/0bab57b4aae355e444444eeecfae5b42?s=400&d=robohash&r=x",
)

// Comment

val commentMock1 = Comment(
        user = userMock1,
        like = 8,
        comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed rhoncus blandit odio, et maximus neque mattis malesuada. Donec aliquam varius velit et sagittis. Nulla at consectetur enim, nec finibus diam. Nulla gravida aliquet sem non congue. Nam et nisl posuere, dignissim lectus sit amet, mattis nunc. Nunc maximus urna ut blandit ultricies. Nam in turpis pellentesque, placerat arcu a, fringilla erat.",
        date = LocalDate.parse("2019-06-11")
)

val commentMock2 = Comment(
        user = userMock2,
        like = 3,
        comment = "Donec ullamcorper neque arcu, vitae maximus nibh mattis et. Cras venenatis nunc iaculis tortor lobortis, vel tempus dui sollicitudin. Donec sagittis eleifend risus vitae luctus. Nullam ac felis sem. Donec vehicula ultricies dui a viverra. In nec faucibus diam. Proin ornare dui nec arcu mattis vehicula. Cras tempus lacinia dui eu posuere. Nunc sit amet aliquet elit, in luctus elit. Morbi vestibulum ex tincidunt eros cursus, ac varius est lacinia.",
        date = LocalDate.parse("2020-11-12")
)

val commentMock3 = Comment(
        user = userMock2,
        like = 10,
        comment = "Nulla vel lorem turpis. Donec et faucibus ligula. Maecenas scelerisque eros sed efficitur pulvinar. Donec finibus egestas libero, et bibendum nulla consectetur a. Maecenas blandit accumsan hendrerit. Duis leo mi, ornare id sem quis, feugiat posuere lectus. Aliquam rutrum rutrum justo sed vulputate. Nam suscipit ipsum a ex mattis, eget ultricies ipsum convallis. Nullam iaculis magna ac tortor mollis tincidunt. Integer in finibus massa. Phasellus lorem ante, sagittis in gravida vitae, consectetur eu ligula. Cras id tincidunt felis. Suspendisse accumsan odio non convallis semper. Sed dapibus, orci vel convallis eleifend, ligula nisl scelerisque purus, a suscipit orci nisi quis est. Ut non tempor arcu, eget auctor turpis. Maecenas fermentum diam vitae ligula imperdiet mollis.",
        date = LocalDate.parse("2018-12-12")
)

// Photo

val photoMock1 = Photo(
        id = "id1",
        title = "Title 1",
        description = "Movie posters from Ghana, Africa.",
        url = "https://i.imgur.com/deVA8K5.jpg",
        score = Score(12, 12),
        views = 360,
        comments = listOf(
        )
)

val photoMock2 = Photo(
        id = "id2",
        title = "Title 2",
        description = "When customers at work try to flirt with me while I'm trying to do my job.",
        url = "https://i.imgur.com/VG9QRT6.jpeg",
        score = Score(560, 110),
        views = 1230,
        comments = listOf(
                commentMock1,
                commentMock2
        )
)

val photoMock3 = Photo(
        id = "id3",
        title = "Title 3",
        description = "Big brother Michael.",
        url = "https://i.imgur.com/Xp9c49b.jpeg",
        score = Score(5000, 100),
        views = 125451,
        comments = listOf(
                commentMock1,
                commentMock2,
                commentMock3
        )
)

// Feed

val feedItemMock1 = FeedItem(
        user = userMock1,
        photo = photoMock1
)

val feedItemMock2 = FeedItem(
        user = userMock2,
        photo = photoMock2
)

val feedItemMock3 = FeedItem(
        user = userMock3,
        photo = photoMock3
)
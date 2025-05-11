package com.chrisp.setaraapp.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.navigation.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {
            CustomTopAppBar()
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            item {
                FeaturedBanner()
            }
            item {
                ProgramSection()
            }
        }
    }
}

@Composable
fun CustomTopAppBar() {
    val magentaColor = colorResource(R.color.magenta_80)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = magentaColor,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 24.dp, 16.dp, 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfilePhotoPlaceholder()
                Spacer(modifier = Modifier.size(16.dp))
                GreetingText(name = "Nadia Niaga")
            }

            IconButton(onClick = { /* Handle notification click */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifikasi",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }

        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
                Text(
                    text = "Cari program",
                    color = Color.LightGray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ProfilePhotoPlaceholder() {
    Surface(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape),
        color = colorResource(R.color.white)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun GreetingText(name: String) {
    Column {
        Text(
            text = "Selamat Datang!!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = name,
            fontSize = 14.sp,
            color = Color.White
        )
    }
}


@Composable
fun FeaturedBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .aspectRatio(16f / 9f),
        shape = RoundedCornerShape(24.dp), // Increased corner radius
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Slightly higher elevation
    ) {
        Image(
            painter = painterResource(id = R.drawable.iklan_homescreen),
            contentDescription = "Featured Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun ServiceCategories() {}

@Composable
fun ProgramSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Pilihan Program SeKerja",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFAA0055),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp,0.dp,16.dp,8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Program cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProgramCard(
                title = "Full Stack Web Dev",
                description = "Belajar menjadi Full Stack Web Developer",
                imageRes = R.drawable.iklan_homescreen,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            ProgramCard(
                title = "Data Science",
                description = "Belajar menjadi Data Scientist",
                imageRes = R.drawable.iklan_homescreen,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ProgramCard(title: String, description: String, imageRes: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(220.dp), // Tinggi Card tetap
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Image
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            // Spacer untuk jarak antara gambar dan judul
            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            // Spacer untuk jarak antara judul dan deskripsi
            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                text = description,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController()
    )
}
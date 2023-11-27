const themeBtn = document.getElementById('theme-toggle')
const state = document.getElementById('state')
const language = document.getElementById('language')
const clock = document.querySelector('.clock')
const hour = clock.querySelector('.time .hour')
const minute = clock.querySelector('.time .minute')
const second = clock.querySelector('.time .second')
const date = clock.querySelector('.date')
const search = document.querySelector('.search')
const searchInput = search.querySelector('input')
const searchList = search.querySelector('ul')
const weather = document.querySelector('.weather')

// theme
const initialTheme = localStorage.getItem('theme') || (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches && 'dark') || 'default';

let currentTheme = 'default'

const swithTheme = theme => {
    document.documentElement.setAttribute('theme', theme)
    localStorage.setItem('theme', theme)
    currentTheme = theme
}

swithTheme(initialTheme)

themeBtn.onclick = () => {
    swithTheme(currentTheme == 'dark' ? 'default':'dark')
}


// clock
const getTime = () => {
    const time = new Date;
    return {
        hour: time.getHours(),
        minute: time.getMinutes(),
        second: time.getSeconds(),
        day: time.toLocaleDateString('en-IN', { weekday: 'long' }),
        date: time.getDate(),
        month: time.getMonth(),
        year: time.getFullYear(),
    }
}

const updateClock = (hourElm, minuteElm, secondElm, dateElm) => {
    const update = () => {
        const { hour, minute, second, date, day, month, year } = getTime()
        hourElm.textContent = hour < 10 ? '0'+hour:hour
        minuteElm.textContent = minute < 10 ? '0'+minute:minute
        secondElm.textContent = second < 10 ? '0'+second:second
        dateElm.textContent = `${day} ${month}, ${year}`
    }
    setInterval(update, 1000)
    update()
}

updateClock(hour, minute, second, date)


// get coords

function getLocation() {
    return new Promise((resolve, reject) => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(resolve, reject);
        } else {
            reject()
        }
    })
}

const getWeather = async (position) => {
    let coords = {
        longitude: position.coords.longitude,
        latitude: position.coords.latitude,
    }
    await fetch('/coords', {
        method: 'POST',
        body: JSON.stringify(coords)
    })
}

// ui
let currentWeather = null;
let lastQuery = null;

const renderWeatherData = ({data, location}) => {
    let address = [location.city, location.state, location.country].filter(e => e).join(", ")
    if(data.name != location.city){
        if(address) address = data.name + ' - ' + address
        else address = data.name
    }
    let html = `<div class="main card">
        <img src="https://openweathermap.org/img/wn/${data.weather[0].icon}@4x.png" height="100"/>
        <h1>${data.main.temp}°F</h1>
        <p>${data.main.temp_min}°F - ${data.main.temp_max}°F</p>
        <small>${address || "Unknow Location"}</small>
    </div>`

    weather.innerHTML = html
}

const fetchWeatherData = async query => {
	if(query == lastQuery) return
    try{
        let response = await fetch("/weather?" + query)
        let data = await response.json();
        renderWeatherData(currentWeather = data)
        lastQuery = query
    }
    catch(e){
       alert("Failed to get weather details")
    }
}

// initialize

getLocation()
.then(async position => {
    let lat = position.coords.latitude
    let lon = position.coords.longitude
    fetchWeatherData(`lat=${lat}&lon=${lon}`)
})

// search
const showLocations = (locations) => {
    searchList.innerHTML = null
    locations.forEach(location => {
        let li = document.createElement('li')
        li.textContent = [location.city, location.state, location.country].filter(e => e).join(", ")
        li.onclick = () => {
            searchList.innerHTML = null
            fetchWeatherData(`lat=${location.coord.latitude}&lon=${location.coord.longitude}`)
        }
        searchList.append(li)
    })
}
const debounce = (callback, seconds) => {
    let timer = null;
    return (...params) => {
        clearTimeout(timer)
        timer = setTimeout(() => callback(...params), seconds * 1000)
    }
}

searchInput.oninput = debounce(async e => {
    let q = searchInput.value;
    searchList.innerHTML = null
    if(q) {
        try{
            let response = await fetch(`/locations?q=${q}&limit=10`)
            let data = await response.json()
            showLocations(data)
        }
        catch(e){
            // empty;
            searchList.innerHTML = null
        }
    }
}, .5)

// language

language.onchange = () => alert("Feather is not available right now!")
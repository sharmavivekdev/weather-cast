<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Weather Cast</title>
    <link rel="stylesheet" href="/css/style.css" />
    <script src="/js/script.js" defer></script>
    <link rel="icon" href="data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 100 100%22><text y=%22.9em%22 font-size=%2290%22>🌤️</text></svg>">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
</head>
<body>
    <header>
        <section class="left">
            <h2>Weather Cast</h2>
        </section>
        <section class="right">
            <div class="option" id="theme-toggle">
                🌙
            </div>
            <div class="option">
                <small>LANGUAGE</small>
                <select id="language">
                    <option>Hindi</option>
                    <option>English</option>
                    <option>French</option>
                    <option>Bengali</option>
                </select>
            </div>
            <div class="option clock">
                <section class="time">
                    <div class="hour">00</div>
                    <span>:</span>
                    <div class="minute">00</div>
                    <span>:</span>
                    <div class="second">00</div>
                </section>
                <section class="date">Thursday, 2023</section>
            </div>
        </section>
    </header>
    <main>
        <div class="search">
            <input list="search-list" type="search" placeholder="Search by City"/>
            <ul></ul>
        </div>
        <div class="weather"></div>
    </main>
</body>
</html>
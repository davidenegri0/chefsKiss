<nav class="navbar navbar-expand-md bg-dark justify-content-center" data-bs-theme="dark">
    <ul class="navbar-nav">
        <li class="nav-item">
            <a class="nav-link" href="/recipesView">Le ricette di Chef's Kiss</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/resturantsList">I Ristoranti</a>
        </li>
        <%if(utente==null){%>
        <li class="nav-item">
            <a class="nav-link" href="/login"><i class='bx bx-log-in bx-sm'></i></a>
        </li>
        <!--<li class="nav-item">
            <a class="nav-link" href="/registration"><i class='bx bxs-user-plus bx-sm'></i></a>
        </li>-->
        <%}else{%>
        <li class="nav-item">
            <a class="nav-link" href="/logout"><i class='bx bx-log-out bx-sm'></i></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/profile"><i class='bx bxs-user bx-sm'></i></a>
        </li>
        <%}%>
        <li class="nav-item">
            <a class="nav-link" href="/homepage"><i class='bx bxs-home bx-sm'></i></a>
        </li>
    </ul>
</nav>

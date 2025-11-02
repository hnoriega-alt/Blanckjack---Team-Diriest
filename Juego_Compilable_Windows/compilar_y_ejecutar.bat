@echo off
echo Compilando archivos Java...
javac ControladorJuego.java Juego.java VistaJuego.java
if %errorlevel% neq 0 (
    echo Error al compilar.
    pause
    exit /b %errorlevel%
)
echo.
echo Ejecutando el programa...
java ControladorJuego
echo.
pause

# AlexAI

**Autor:** Șandru Alexandru

AlexAI este un asistent virtual creat pentru a răspunde la întrebări generale, a genera cod și a susține conversații. Folosind modelul Llama 3.1, AlexAI oferă o experiență interactivă printr-o aplicație mobilă Android și un server FastAPI care interacționează cu modelul de limbaj Llama 3.1.

## Descrierea Proiectului

Acest proiect este structurat în două componente separate, dar interconectate:

1. **Aplicația Android** (`AlexAI`): O aplicație pe andoid care permite utilizatorilor să interacționeze cu asistentul virtual AlexAI, să pună întrebări și să primească răspunsuri generate de Llama 3.1.
   
2. **Serverul Llama 3.1** (`AlexAI-server`): Un server FastAPI care rulează modelul Llama 3.1 folosind Ollama. Acest server se ocupă de procesarea cererilor și furnizarea răspunsurilor prin intermediul unui API, care este accesat de aplicația Android.

## Cum se folosește

### Pași pentru a rula aplicația

1. **Configurați serverul Llama 3.1**:
   - Înainte de a rula aplicația Android, trebuie să pornești serverul FastAPI pentru modelul Llama 3.1, care este responsabil pentru generarea răspunsurilor.
   
   - Pentru a configura și porni serverul, te rog să urmezi pașii din fișierul `README.md` din directorul `AlexAI-server`.

2. **Rularea aplicației Android**:
   - După ce serverul este pornit, poți lansa aplicația Android din directorul `AlexAI`. Aplicația va interacționa cu serverul Llama pentru a obține răspunsuri la întrebările utilizatorilor.

### Pași pentru a instala și rula serverul (detalii în `AlexAI-server/README.md`)

1. Clonează repository-ul folosind comanda:
```bash
git clone https://github.com/Xhadrines/ProiectCMo.git
```

2. Activează serverul FastAPI pentru modelul Llama 3.1, așa cum este descris în fișierul `README.md` din directorul `AlexAI-server`:
   - Creează un mediu virtual Python.
   - Instalează dependențele.
   - Instalează Ollama și modelul Llama 3.1.
   - Lansează serverul FastAPI.

3. După ce serverul este activ, poți deschide aplicația Android. Aplicația va trimite cereri la server pentru a obține răspunsuri.

**Notă**: Asigură-te că ai activat serverul FastAPI și modelul Llama 3.1 înainte de a lansa aplicația Android pentru a evita erorile de conexiune.

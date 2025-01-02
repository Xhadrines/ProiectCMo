# AlexAI-server

**Autor:** Șandru Alexandru

AlexAI-server este o componentă adițională pentru aplicației mobile AlexAI, un asistent virtual care răspunde la întrebări generale, generează cod și susține conversații utilizând modelul Llama 3.1. Această aplicație FastAPI permite interacționarea cu modelul Llama 3.1 printr-un API, care poate fi folosit de aplicația Android AlexAI pentru a obține răspunsuri.

## Cerințe de sistem

### Cerințe minime

* **Placă video:** NVIDIA RTX 3050 Ti cu 4 GB VRAM
* **Procesor:** AMD Ryzen 7 6800H
* **Memorie RAM:** 16 GB DDR5
* **Sistem de operare:** Windows 10 sau mai recent

### Cerințe recomandate

* **Placă video:** NVIDIA RTX 4070 cu 8 GB VRAM
* **Procesor:** AMD Ryzen 7 7745HX
* **Memorie RAM:** 32 GB DDR5
* **Sistem de operare:** Windows 10 sau mai recent

## Instalare

1. Clonează repository-ul folosind comanda:

```bash
git clone https://github.com/Xhadrines/ProiectCMo.git
```

2. Creează și activează un mediu virtual folosind comenzile:

```bash
python -m venv venv
```

```bash
.\venv\Scripts\activate
```

3. Instalează dependențele folosind comanda:

```bash
pip install -r requirements.txt
```

4. Instalează Ollama mergând pe [site-ul oficial Ollama](https://ollama.com/).

5. Instalarea modelului Llama 3.1 folosind comanda:

```bash
ollama pull llama3.1
```

## Pornirea serverului
După ce ai instalat toate dependențele și ai configurat Ollama, porneste serverul FastAPI folosind comanda:

```bash
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

*Dacă vrei să îl accesezi local, după ce a fost pornit intră pe http://127.0.0.1:8000/docs.*
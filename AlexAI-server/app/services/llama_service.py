import ollama
from fastapi import HTTPException

def get_chat_response(question: str, language: str) -> str:
    """
    Functie care trimite o intrebare catre modelul Llama 3.1 si returneaza raspunsul.
    
    Parametrii:
    - question (str): intrebarea pe care dorim sa o adresam modelului.
    - language (str): limba intrebarii, poate fi 'ro' sau 'en'.
    
    Returneaza:
    - Raspunsul modelului Llama 3.1 sau un mesaj de eroare.
    """
    
    try:
        # Validarea limbii
        if language not in ['ro', 'en']:
            raise HTTPException(status_code=400, detail="Limba nu este disponibila. Permise: 'ro' sau 'en'.")

        model = "llama3.1"
        
        # Definirea contextului in functie de limba aleasa
        if language == 'ro':
            context = "Raspunde doar in limba romana si nu intelege alte limbi."
        elif language == 'en':
            context = "Respond only in English and do not understand other languages."

        # Apelam API-ul Ollama pentru a obtine raspunsul
        response = ollama.chat(
            model=model, 
            messages=[{"role": "system", "content": context}, {"role": "user", "content": question}]
        )

        # Verificam daca raspunsul este valid
        if "message" in response and "content" in response["message"]:
            return response["message"]["content"]
        else:
            raise HTTPException(status_code=500, detail="A aparut o eroare la obtinerea raspunsului.")

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"A aparut o eroare: {str(e)}")

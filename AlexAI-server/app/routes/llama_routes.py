from fastapi import APIRouter, Query, HTTPException
from app.services.llama_service import get_chat_response

router = APIRouter()

@router.get("/ask", tags=["Llama 3.1"])
def ask_llama(
    question: str = Query(..., min_length=1, max_length=500, description="Intrebarea pe care o adresezi modelului."),  # Validare pentru intrebare
    language: str = Query(..., regex="^(ro|en)$", description="Limba intrebarii: 'ro' pentru romana, 'en' pentru engleza.") # Validare pentru limba
):
    """
    Endpoint pentru a trimite o intrebare catre modelul Llama 3.1 si a obtine raspunsul.
    
    Parametrii:
    - question (str): intrebarea pe care dorim sa o adresam modelului.
    - language (str): limba intrebarii, poate fi 'ro' sau 'en'.
    
    Returneaza:
    - Raspunsul modelului Llama 3.1.
    """
    
    # Verificam daca intrebarea a fost furnizata
    if not question:
        raise HTTPException(status_code=400, detail="Intrebarea nu poate fi goala.")
    
    response = get_chat_response(question, language)
    return {"response": response}

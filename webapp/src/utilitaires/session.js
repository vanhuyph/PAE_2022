import VerifierTokenUtilisateur from "./VerifierTokenUtilisateur";

const STORE_NAME = "utilisateur";

const recupUtilisateurDonneesSession = () => {
  let retrievedUser = localStorage.getItem(STORE_NAME);
  if (!retrievedUser) {
    retrievedUser = sessionStorage.getItem(STORE_NAME);
  }
  return JSON.parse(retrievedUser);
};

const creationDonneeSessionUtilisateur = (user, isRemember) => {
  const storageValue = JSON.stringify(user);
  if (isRemember) {
    localStorage.setItem(STORE_NAME, storageValue);
  } else {
    sessionStorage.setItem(STORE_NAME, storageValue);
  }
};

const enleverDonneeSession = () => {
  localStorage.removeItem(STORE_NAME);
  sessionStorage.removeItem(STORE_NAME);
};

const verifierTokenAuChargement = () => {
  let tokenRecupere = localStorage.getItem(STORE_NAME)
  let tokenLocal = true
  if (!tokenRecupere) {
    tokenRecupere = sessionStorage.getItem(STORE_NAME)
    tokenLocal = false
  }
  if (!tokenRecupere) {
    return;
  }
  VerifierTokenUtilisateur(JSON.parse(tokenRecupere), tokenLocal)
}

export {
  recupUtilisateurDonneesSession,
  creationDonneeSessionUtilisateur,
  enleverDonneeSession,
  verifierTokenAuChargement,
};

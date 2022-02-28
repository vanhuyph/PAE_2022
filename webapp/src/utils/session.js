const STORE_NAME = "utilisateur";


const recupUtilisateurDonneesSession = () => {
  let retrievedUser = localStorage.getItem(STORE_NAME);
  if (!retrievedUser) retrievedUser = sessionStorage.getItem(STORE_NAME);
  return JSON.parse(retrievedUser);
};

const creationDonneeSessionUtilisateur = (user, isRemember) => {
  const storageValue = JSON.stringify(user);
  if(isRemember) {
    localStorage.setItem(STORE_NAME, storageValue);
  } else{
    sessionStorage.setItem(STORE_NAME, storageValue);
  }
};

const enleverDonneeSession = () => {
  localStorage.removeItem(STORE_NAME);
  sessionStorage.removeItem(STORE_NAME);

};

export {
  recupUtilisateurDonneesSession,
  creationDonneeSessionUtilisateur,
  enleverDonneeSession,

};
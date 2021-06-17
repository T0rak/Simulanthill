# Simulanthill

Simulanthill est un programme permettant de simuler le comportement de fourmillères au sein d'un écosystème. 

## Mode d'emploi

### Prérequis
Pour pouvoir utiliser SimulantHill, votre ordinateur doit être doté de : 
- Une version de Java Runtime Environnement (JRE) supérieure ou égale à 18.9. 
- L’archive jar « simulanthill.jar ».

### Lancement de l’application
Pour lancer SimulantHill, ouvrez un terminal et utilisez la commande ‘’java -jar ‘’ suivi de l’emplacement et du nom du fichier jar « simulanthill.jar ». 
Exemple avec Windows Power Shell: 



NB : cet exemple sous-entend que le .jar se trouve dans le dossier « Soft » sur le disque « C: »


### Interface du simulateur
L’interface du simulateur peut se décomposer en deux parties : une partie d’affichage du simulateur et une partie de gestion des commandes.

La partie d’affichage du simulateur permet de visualiser l’état et l’évolution de la simulation tandis que la partie de gestion des commandes permet d’interagir avec la simulation.


### Importer une carte
Si vous désirez importer une carte valide dans SimulantHill pour analyser le comportement des fourmis dans votre écosystème créé, vous pouvez utiliser le bouton « Load Map » dans la partie de gestion des commandes.

Une fois le bouton pressé, vous serez invité à sélectionner le fichier .txt contenant votre carte. Une fois la carte sélectionnée, elle sera vérifiée et en cas de vérification réussite, elle sera chargée sur la partie d’affichage du simulateur. 
Attention : pour qu’une carte soit valide :
- Elle ne doit avoir que des caractères valides. La liste des caractères valides sont les suivants : 
	- « R » : définir une ressource.
	- « # » : définir un obstacle.
	- « O » : définir une fourmilière.
- Elle doit être entourée d’obstacles.
- La longueur des lignes doivent être identiques.

### Générer une carte aléatoire
Il est tout à fait possible d’utiliser SimulantHill sans importer de cartes. Par défaut, l’application génère une carte aléatoire au lancement. Si vous souhaitez regénérer une nouvelle carte aléatoire, il suffit d’appuyer sur le bouton « Generate Map » dans la partie de gestion des commandes.

### Lancer la simulation
Pour lancer la simulation, il suffit d’appuyer sur le bouton « Play » dans la partie de gestion des commandes.

### Modifier les paramètres du simulateur
Que ce soit avant ou pendant une simulation en cours, vous pouvez modifier les paramètres du simulateur avec les éléments présents dans la partie de gestion des commandes. Parmi ces éléments, il vous est possible de :
- Modifier la durée de vie des phéromones (temps en millisecondes entre 0 et 3000).
- Modifier le temps de création d’une nouvelle phéromone par les fourmis (temps en millisecondes entre 0 et 20).
- Déterminer la distance à laquelle une fourmi peut détecter des phéromones (en unité logique entre 0 et 5). Plus le nombre est grand, plus la fourmi pourra détecter des phéromones distantes mais aura un impact sur la fluidité du simulateur.
- Déterminer le nombre de fourmis (entre 1 et 10’000).
- Déterminer l’indépendance d’une fourmi (sur une échelle entre 0 et 100). Plus l’indépendance est grande, plus elle est à même d’ignorer les ressources et phéromones.
- Déterminer la vitesse de déplacement des fourmis (entre 0.0 et 2.0 avec comme valeur par défaut 1.0).
Dans le cas où vous souhaiteriez revenir aux paramètres par défaut, le bouton « Reset Parameters » permet de réinitialiser les valeurs des commandes.
Par défaut, les phéromones ne sont pas visibles sur la partie d’affichage du simulateur. Il est possible de les rendre visible en modifiant leur opacité avec le curseur de défilement « Pheromone opacity ».

### Réinitialiser la simulation
Pour réinitialiser la simulation, il suffit de presser sur le bouton « Reset » au bas de l’application.

### Mettre en pause la simulation
Pour mettre en pause la simulation, le bouton « Pause » au bas de l’écran permet de le faire. Si vous souhaitez la reprendre, vous pouvez appuyer à nouveau sur le même bouton.




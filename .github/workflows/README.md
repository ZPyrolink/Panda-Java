# Workflow

## Description:
Vous trouverez ici les explications des fichier .yml de ce dossier.

## Test et Couverture de code

**Fichier** : *test.yml*

Ce fichier est décomposé en deux parties : Les tests et la couverture de code. Il agit sur toutes les branches sauf **main** qui sont géré par un pipeline différent.


### Les tests

Les tests sont réalisé par maven et la librarie JUnit. Il s'agit d'exécuter tout les tests présent dans le fichier
/src/test. Le pipeline étant intérrompu si un des tests échoue.

### La couverture de code

Une fois les tests passé, on vérifie qu'il couvrent une partie sufisante du code. Dans notre cas, la proportion doit
être supérieure à 80% (cf : *pom.xml*). Un rapport de couverture de code est ensuite généré. Dans le cas ou la
couverture de code est insufisante, le pipeline s'arrête et renvoie une annotation d'erreur.

## Pipeline de la branche principale

**Fichier** : *workflow.yml*

Ce fichier est un pipeline qui agit sur la branche **main**, pour le moment ce fichier fait la même chose que *test.yml* mais sur la branche **main**.
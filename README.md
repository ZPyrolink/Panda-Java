# Implémentation Java des pandas
<a href="https://github.com/users/ZPyrolink/projects/2" alt="Tests"><img src="https://img.shields.io/badge/Gérer%20avec-GitHubProject-blue.svg"/> </a>
<a href="https://pandas.pydata.org/" alt="Tests"><img src="https://img.shields.io/badge/Source-Panda-purple.svg"/> </a>
<a href=""><img src="https://img.shields.io/badge/Powered%20By-Java-ED8B00"/> </a>



## Description

**Pandas** est un paquet Python qui fournit des données rapides, flexibles et expressives
des structures conçues pour rendre le travail avec des données "relationnelles" ou "étiquetées"
facile et intuitif. Il vise à être l’élément de base de haut niveau pour
faire de l’analyse de données pratique. Notre travail est donc de créer une implémentation Java de ce paquet Python.


## Table des matières

- [Fonctionalités](#Fonctionalités)
- [Code source](#Code-source)
- [Background](#background)

## Fonctionalités
Les Dataframes sont des tableaux en deux dimensions où chaque colonne est identifiée par un label et chaque ligne par un index.
Notre implémentation de notre dataframe est une map nos label servant de clé vers les colonnes et notre fonctions get() pouvant prendre un int en entré pour donner une ligne.
Chaque colonne stocke des données d’un seul type. Cependant deux colonnes différentes peuvent stocker des types différents.

**Création du Dataframe**

- A partir d’un constructeur prenant en paramètre le contenu de chaque colonne sous forme d’une
structure de données simple (par exemple un tableau).
- A partir d’un constructeur prenant en paramètre le nom d’un fichier CSV 

**Affichage d’un dataframe** : permet d'afficher les Dataframes. Il exites plusieurs vairantes de cette
méthode.
- affichage du dataframe en entier

**Selection de sous-ensemble** : permet de créer un nouveau Dataframe à partir d'une selection
d'un sous-ensemble d'un Dataframe.
- sélection d'un sous-ensemble de lignes
- sélection d'un sous-ensemble de colonnes

## Code source
Le code source python est conserver sur Github à l'url:
https://github.com/pandas-dev/pandas

Ainsi que notre implémentation à l'url:
https://github.com/ZPyrolink/PandaJava


## Background
Le travail sur ``panda`` commence avec [AQR](https://www.aqr.com/) en 2008



[Go to Top](#Table-des-matières)

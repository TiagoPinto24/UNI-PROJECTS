import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split

from trabalho import KNNeighborsUE
from trabalho import NBayesUE

#ler o ficheiro csv
data = pd.read_csv('iris.csv')

#separar atributos das classes
X = data.values[:, 0:-1]
y = data.values[:, -1]

# criar conjs de treino e teste
X_train, X_test, y_train, y_test = train_test_split(X, y,test_size=0.25, random_state=5)

#juntar atributos e classes
train = np.insert(X_train, len(X_train[0]), y_train, axis=1)
test = np.insert(X_test, len(X_test[0]), y_test, axis=1)

#algoritmo KNN
KNN = KNNeighborsUE(k=5, p=2.0)
KNN.fit(X_train, y_train)
predictionsKNN = KNN.predict(X_test)

#algoritmo Naive Bayes
NBayes=NBayesUE(suave=1e-9)
NBayes.fit(X_train,y_train)
predictionsNB = NBayes.predict(X_test)

#mostrar os resultados
print('score do KNNeighbors:', KNN.score(predictionsKNN,y_test))
print('score do NBayes: ',NBayes.score(predictionsNB,y_test))


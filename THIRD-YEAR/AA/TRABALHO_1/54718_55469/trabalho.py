import numpy as np

class KNNeighborsUE:

    #inicialização
    def __init__(self,k=5,p=2.0):
        self.k=k
        self.p=p

    #fit do KNN
    def fit(self,X_train,Y_train):
        self.X_train = np.array(X_train)
        self.Y_train = np.array(Y_train)
        return self

    def predict(self,X_test):
        #array onde será guardado as classes atribuidas ao grupo de teste
        predictions=[]

        for x in X_test:
            # array onde são guardadas as distancias duma instancia x de teste 
            # para todas as instancias do conjunto de treino
            distances_array=[]
            for attributes in self.X_train: 
                distances_array.append(self.distance(x,attributes))
            #este array agora passa a ser apenas os indices das k distancias mais próximas
            distances_array=np.argsort(distances_array)[:self.k]
            for d in distances_array:
                #array onde guardamos as classes correspondentes aos indices do array distances_array
                nearest_neighbors=[]
                nearest_neighbors.append(self.Y_train[d])
            predictions.append(max(nearest_neighbors))

        return np.array(predictions)

    #distancia relativa ao valor de p
    def distance(self,a_array,b_array):
        return np.pow(np.sum(abs(a_array - b_array)**self.p),(1/self.p))
    
    #score do KNN (corretos/total)
    def score(self, predictions, Y_test):
        return np.sum(predictions==Y_test)/len(Y_test)

class NBayesUE:

    #inicialização
    def __init__(self,suave=1e-9):
        self.suave=suave

    #fit do Naive Bayes
    def fit(self,X_train,Y_train):
        self.x_train = np.array(X_train)
        self.y_train = np.array(Y_train)

        #Cálculo da probabilidades das classes
        classes = np.unique(self.y_train)
        Py = [] #Array onde são guardadas as probabilidades de cada classe

        for i in range(classes.size):
            #cálculo da probabilidade
            class_probability = np.log(self.suave+np.count_nonzero(self.y_train==classes[i]))-np.log(self.y_train.size+self.suave*classes.size)
            Py.append(class_probability)
        self.Py=np.array(Py)

        #Cálculo da media e da variancia de cada atributo relativamente a cada classe
        means = []
        variances = []
        train = np.insert(X_train, len(X_train[0]), Y_train, axis=1)

        for class_ in classes:
            respective_class = train[train[:, -1] == class_, :-1] 
            #calculo da média 
            mean = np.mean(respective_class, axis=0)
            #cálculo da variancia mais o suave, para evitar valores a 0
            variance = (np.var(respective_class, axis=0)) + self.suave
            means.append(mean)
            variances.append(variance)
        self.means = np.array(means)
        self.varainces = np.array(variances)

        return self
    
    def predict(self, X_test):
        #array onde será guardado as classes atribuidas ao grupo de teste, semelhante ao KNN
        predictions = []

        for x in X_test:
            #array onde se guardam as probabilidades de x pertencer a cada classe
            probability_array = []
            for class_ in range(np.unique(self.y_train).size):
                #Array onde se guarda as probabilidades de cada atributo relativos a uma só classe
                attributes_probability = []
                for attribute in range(X_test[0].size):
                    #cálculo da probabilidade de cada atributo numa classe 
                    probability = (self.class_probability(x[attribute], self.means[class_][attribute], self.varainces[class_][attribute]))
                    attributes_probability.append(probability)
                attributes_probability.append(self.Py[class_])
                probability_array.append(np.sum(attributes_probability))
            #insert em predictions a classe mais provável
            predictions.append(np.unique(self.y_train)[probability_array.index(max(probability_array))])
        
        return np.array(predictions)
    
    #fórmula modificada da probabilidade pelas razões explicadas no relatório
    def class_probability(self, Xn, mean, variance):
        exp = np.pow((Xn-mean)/variance, 2)
        base =(-1/2)*np.log(np.pi *2*np.pow(variance,2))
        return base - exp
    
    #score do Naive Bayes (corretos/total)
    def score(self, predictions, Y_test):
        return np.sum(predictions==Y_test)/len(Y_test)
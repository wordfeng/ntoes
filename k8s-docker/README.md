###  jib项目
https://github.com/GoogleContainerTools/jib/tree/master/examples/spring-boot
```bash
### 编译到本地docker
mvn compile jib:dockerBuild
### 编译到远程仓库  k8s需要到远程仓库拉取镜像
mvn compile jib:build -Djib.to.auth.username=wrdfeng -Djib.to.auth.password=**********
```
### 具体配置信息参考pom.xml
### 基于minikube  

```bash
kubectl create deployment test-kube  --image=wrdfeng/learning
kubectl delete deployment test-kube

kubectl expose deployment test-kube --type=NodePort --port=8888
kubectl delete services test-kube
```

### EXTERNAL-IP
```bash
kubectl get services test-kube
```

### minikube随机一个端口启动浏览器打开服务

```bash
minikube service test-kube
```

### 指定端口转发 outsidePort:internalPort

```bash
kubectl port-forward service/test-kube 8888:8888
# /api/images/test?id=123
```




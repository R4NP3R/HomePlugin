# Home Plugin

Plugin para teletransportar para sua casa de maneira simples, usando comandos fáceis de lembrar e com o delay para teletransportar personalizavel.

## Download

Caso deseje baixar o Plugin para uso [`HomePlugin`](https://github.com/R4NP3R/HomePlugin/tree/main/build/libs).

## Comandos

* /home help - para ver os comandos /home do plugin
* /home - usado para teleportar para sua casa
* /home particle - ver a particula que o player está usando
* /home particles list - lista as particulas disponiveis para uso
* /home particles <particle> - para selecionar a particula desejada
* /home particles off - desativa as particulas do player
* /home delay - retorna a quantidade de segundos do delay
* /home delay <seconds> - altera a quantidade de segundos do delay
* /sethome - cria uma nova home ou atualiza a localização para o player

## Script

Caso deseje usar um script para facilitar na hora de enviar o plugin para pasta do seu servidor use o [`CopyJar`](https://github.com/R4NP3R/HomePlugin/blob/main/build.gradle), lá no build.gradle apenas seleciona a pasta onde deseja enviar o mod, Exemplo:

```sh
task copyJar {
    copy {
        from 'build/libs/WindChargeControl-1.0.jar'
        into 'C:\Users\{USER}\Desktop\server\plugins'
    }
}

```





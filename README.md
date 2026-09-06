# 🎮 projeto_pokemon_TPA

Projeto desenvolvido como atividade acadêmica, composto por um aplicativo mobile e um backend responsável pela autenticação e comunicação com a aplicação.

## 📌 Sobre o projeto

O `projeto_pokemon_TPA` é uma aplicação baseada em uma arquitetura separada entre frontend mobile e backend.

O projeto é composto por:

- 📱 **App:** desenvolvido com Expo e React Native.
- ⚙️ **Backend:** desenvolvido com Spring Boot.
- 🔐 **Autenticação:** realizada utilizando JWT armazenado em cookie `HttpOnly`.

A principal alteração realizada no backend foi a adaptação da autenticação para utilizar cookies, evitando o envio e armazenamento manual do token JWT pelo aplicativo.

---

## 🏗️ Estrutura do projeto

```text
projeto_pokemon_TPA/
├── app/          # Aplicação Expo / React Native
│   ├── assets/
│   ├── src/
│   ├── package.json
│   └── ...
│
├── backend/      # API Spring Boot
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── .gitignore
└── README.md
 ```


## ▶️ Como executar o projeto
- Entre na pasta do aplicativo:

- cd app

- Instale as dependências:

npm install

- Inicie o Expo:

npx expo start

## ⚙️ Backend

- Entre na pasta do backend:

- cd backend

- Execute a aplicação utilizando o Maven:

./mvnw spring-boot:run

No Windows, caso necessário:

mvnw.cmd spring-boot:run

## 🧪 Testes e demonstração

O funcionamento da aplicação e da autenticação foi testado, incluindo o fluxo de login e comunicação entre o aplicativo e o backend.

🎥 Demonstrações

Os vídeos de demonstração mostram o funcionamento do projeto e servem como evidência dos testes realizados:
Drive: https://drive.google.com/drive/folders/1htGLJE9Uk4lzpuyt0JMAXJzyNO4st5kI?usp=sharing


## 👩‍💻 Autores
- Gabrielly Nascimento
- Maria Eduarda Monteiro Viana — GitHub: https://github.com/MaariaMonteiro


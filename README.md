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

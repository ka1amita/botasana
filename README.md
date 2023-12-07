# Botasana chatbot

## Introduction

I

## Description

## Try it!

### Using Docker image

> [!Note]
> You will need your own [OpenAI API key](https://platform.openai.com/docs/quickstart/account-setup) to run it locally. Otherwise check my [homepage](ka1amita.github.io) if there is currently a live version running.

1. Download the image from Docker Hub and run it locally.
   ```shell
   docker run -dp 127.0.0.1:8080:8080 -e SECRET_OPENAI_API_KEY=<your-openai-api-key> ka1amita/botasana
   ```
1. Open [127.0.0.1:8080](http://127.0.0.1:8080) in your browser.
1. Send your prompt.

## Tech Stack

+ [x] OpenAI API <img alt="OpenAI" class="icon" style="height: 20px" src="readme-img/openai.svg"/>
+ [x] Spring Boot 
+ [x] PostgreSQL
+ [x] Java
+ [x] Log4j
+ [x] Jupiter
+ [x] Docker
+ [X] HMTL, CSS, JS
+ [ ] HTMX
+ [ ] Terraform
+ [ ] AWS
+ [ ] GitHub Acitons
+ [ ] ELK

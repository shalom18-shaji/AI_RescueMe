# 🚨 RescueMe — AI Emergency Response Assistant

## 📌 Overview

RescueMe is a college CIA prototype inspired by the research paper:

**"Rescue Me: AI Emergency Response and Disaster Management System."**

This project implements the **AI/NLP emergency-response chatbot** component, where users describe an emergency and receive its type, severity, and safety recommendations.

---

## 📸 Application

### Dashboard

![Dashboard](screenshots/home.jpeg)

### Emergency Analysis

![Accident Analysis](screenshots/accident.jpeg)

---

## 🎥 Demo Walkthrough

The demo demonstrates:

- Fire emergency
- Accident emergency
- AI classification
- Severity analysis
- Safety recommendations

**Demo Video:**  
[▶️ Watch Demo Video](https://drive.google.com/drive/folders/1f3CBtEb52ax8AIqM8wjYuL0-BhtcSoeW?usp=sharing)

---

## 🛠️ Tech Stack

- **Frontend:** React, Vite, Tailwind CSS
- **Backend:** Java 17, Spring Boot, Maven
- **AI:** Google Gemini API

---

## 🏗️ Architecture

```text
User
  ↓
React Frontend
  ↓
Java Spring Boot
  ↓
Gemini AI
  ↓
Emergency Analysis
  ↓
React Dashboard

⚙️ Working
User enters an emergency description.
React sends the request to the Java Spring Boot backend.
Gemini AI analyzes the emergency description.
The backend processes the AI response.
The dashboard displays the emergency type, severity, and safety recommendations.
Sample

Input:

"There is a fire on the second floor and there is heavy smoke."

Output:

🔥 Type: Fire
⚠️ Severity: High
📋 Action: Evacuate safely, avoid smoke, and contact emergency services.


📄 Research Paper

Rescue Me: AI Emergency Response and Disaster Management System

2024 2nd International Conference on Artificial Intelligence and Machine Learning Applications (AIMLA)

DOI: 10.1109/AIMLA59606.2024.10531386

The implementation is based on the paper's proposed NLP/chatbot functionality for emergency communication and assistance.


👨‍💻 Student Details
Name: KOSHY SHALOM SHAJI
Roll No: 5024131
Class/Division: IT-3rd Year
Subject: Artificial Intelligence
Academic Year: 2026–27
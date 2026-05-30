# Panini Support - Proof of Concept

Mobile application proof of concept for managing support tickets related to the FIFA World Cup 2026 album distribution.

## Description

Internal support ticket system for Panini that allows:
- Simulated authentication
- Support ticket listing
- New ticket creation
- Ticket detail view
- Status and priority updates

## Technologies

- **Kotlin** - Main language
- **Jetpack Compose** - Modern UI with Compose
- **MVVM** - Architectural pattern
- **Navigation Compose** - Screen navigation
- **StateFlow** - Event-based communication
- **Retrofit** - Preparation for future API

## Project Structure

```
Exam-2-Fabian-Sanchez/
├── app/                    # Android project
├── contracts/               # API contracts (YAML)
├── docs/                   # Technical documentation
├── video/                   # Demo video link
└── README.md
```

## Login Credentials

- **Email**: admin@gmail.com
- **Password**: Admin1234#

## Build

```bash
cd Exam-2-Fabian-Sanchez
./gradlew assembleDebug
```

## Running

1. Open Android Studio
2. Import the project
3. Run on emulator/device

## Notes for Developers

- Data is mocked and does not require a backend
- Network structure is prepared for future API integration
- Feature Flags allow controlling functionality without code changes
- StateFlow-based communication enables automatic UI updates

## Feature Flags

- `ENABLE_TICKET_CREATION` - Controls ticket creation FAB visibility
- `ENABLE_PRIORITY_UPDATE` - Controls priority update functionality

## API Contracts

See individual YAML files in `/contracts/` directory.
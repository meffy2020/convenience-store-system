# AI 프롬프트 사용 기록

이 문서는 AI 어시스턴트와 함께 편의점 재고 관리 시스템을 개발하면서 사용한 주요 프롬프트와 그 사용 의도를 기록한 것입니다.

## 1. 초기 프로젝트 설정 및 Phase 1 구현

> **Prompt:** "Okay, I've reviewed the homework assignment. It's a comprehensive task to build a smart inventory management system for a convenience store using Kotlin. The project is divided into three phases, starting with a basic system and progressively adding more advanced features like data analysis and extensibility. I'm ready to get started. What would you like me to do first? I can begin by setting up the project structure as outlined in the PDF, creating the necessary directories and files."

- **의도:** AI에게 과제 내용을 인지시키고, 프로젝트의 첫 단계인 기본 구조 설정을 요청했습니다. 이를 통해 AI가 전체적인 맥락을 이해하고 다음 단계를 준비하도록 유도했습니다.

> **Prompt:** "Based on my analysis of the homework, there's a discrepancy between the provided sample data and the expected output. I'll proceed with the assumption that in the `Product` data class, `stock` represents the ideal stock, and `safetyStock` represents the current stock. The report is generated from the initial data, and the sales data will be used for the next cycle. I'll now implement the logic for generating the reports in `Main.kt`, starting with the urgent stock alerts."

- **의도:** 과제 PDF의 데이터 불일치 문제를 발견하고, 이를 해결하기 위한 명확한 가설(stock -> 적정재고, safetyStock -> 현재재고)을 AI에게 제시했습니다. 이를 통해 모호한 부분을 명확히 하고, 일관된 로직으로 구현을 진행하도록 지시했습니다.

## 2. Phase 2: 확장 함수를 이용한 기능 추가

> **Prompt:** "Phase 1 is complete. Now for Phase 2: Smart Analysis. I'll use extension functions in `ProductExtensions.kt` to avoid modifying the `Product` class. My plan is to first calculate the updated stock after sales. Then, I'll create extension functions for `isStockLow()`, `getInventoryTurnoverRate()`, and `getSalesEfficiency()`. Finally, I'll use these in `Main.kt` to generate the 'Management Analysis Report' and 'Overall Operation Status' report. I'll start by implementing the `isStockLow` extension function."

- **의도:** Phase 2의 핵심 요구사항인 '기존 클래스 수정 없이 기능 추가'를 해결하기 위해 '확장 함수'라는 구체적인 기술을 사용하도록 명시했습니다. 또한, 구현할 기능들을 명확히 나열하여 AI가 체계적으로 코드를 작성하도록 안내했습니다.

## 3. Phase 3: 상속과 인터페이스를 이용한 리팩토링

> **Prompt:** "Phase 2 is done. Now for Phase 3: Extensible System Design. I'll refactor the `Product` class into an interface with common properties and a `display()` method. Then, I'll create concrete classes like `Food`, `Beverage`, and `HouseholdGood` implementing `Product` with their own specific properties. All inventory logic from `Main.kt` will move to a new `InventoryManager` class that manages a list of `Product`s. `Main.kt` will then just instantiate and use `InventoryManager`. I'll start by defining the `Product` interface in `AdvancedProduct.kt`."

- **의도:** Phase 3의 목표인 '확장 가능한 구조'를 구현하기 위해 인터페이스, 상속, 그리고 관리자 클래스(`InventoryManager`) 도입이라는 구체적인 리팩토링 계획을 제시했습니다. 이는 전체적인 프로그램의 구조를 어떻게 변경할 것인지 명확하게 지시하여, AI가 큰 그림을 이해하고 코드를 재구성하도록 하기 위함이었습니다.

> **Prompt:** "In the AdvancedProduct.kt file, I will refactor the code to move the common logic for `isStockLow`, `getInventoryTurnoverRate`, and `getSalesEfficiency` into the `Product` interface as default implementations. This will reduce code duplication in the concrete classes."

- **의도:** 하위 클래스들에서 중복되는 코드를 발견하고, 이를 인터페이스의 기본 구현(default implementation)으로 옮겨 코드 중복을 제거하고 유지보수성을 높이도록 지시했습니다. 이는 좋은 객체지향 설계 원칙을 코드에 적용하기 위한 것이었습니다.

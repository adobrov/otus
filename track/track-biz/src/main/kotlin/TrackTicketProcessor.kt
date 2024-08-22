package ru.otus.otuskotlin.track.biz

import ru.otus.otuskotlin.track.biz.general.initStatus
import ru.otus.otuskotlin.track.biz.general.operation
import ru.otus.otuskotlin.track.biz.general.stubs
import ru.otus.otuskotlin.track.biz.stubs.*
import ru.otus.otuskotlin.track.biz.validation.*
import ru.otus.otuskotlin.track.biz.repo.*
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.TrackCorSettings
import ru.otus.otuskotlin.track.common.models.TrackTicketId
import ru.otus.otuskotlin.track.common.models.TrackTicketLock
import ru.otus.otuskotlin.track.common.models.TrackCommand
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.cor.rootChain
import ru.otus.otuskotlin.track.cor.worker
import ru.otus.otuskotlin.track.cor.chain

class TrackTicketProcessor(
    private val corSettings: TrackCorSettings = TrackCorSettings.NONE
) {
    suspend fun exec(ctx: TrackContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<TrackContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

        operation("Создание заявки", TrackCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в ticketValidating") { ticketValidating = ticketRequest.deepCopy() }
                worker("Очистка id") { ticketValidating.id = TrackTicketId.NONE }
                worker("Очистка заголовка") { ticketValidating.subject = ticketValidating.subject.trim() }
                worker("Очистка описания") { ticketValidating.description = ticketValidating.description.trim() }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateSubjectHasContent("Проверка символов")
                validateDescriptionNotEmpty("Проверка, что описание не пусто")
                validateDescriptionHasContent("Проверка символов")
                finishTicketValidation("Завершение проверок")
            }
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание объявления в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Получить объявление", TrackCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adValidating") { ticketValidating = ticketRequest.deepCopy() }
                worker("Очистка id") { ticketValidating.id = TrackTicketId(ticketValidating.id.asInt()) }
                validateIdNotEmpty("Проверка на непустой id")

                finishTicketValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика чтения"
                repoRead("Чтение заявки из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { operationState == TrackOperationState.RUNNING }
                    handle { ticketRepoDone = ticketRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
        }
        operation("Изменить объявление", TrackCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adValidating") { ticketValidating = ticketRequest.deepCopy() }
                worker("Очистка id") { ticketValidating.id = TrackTicketId(ticketValidating.id.asInt()) }
                worker("Очистка lock") { ticketValidating.lock = TrackTicketLock(ticketValidating.lock.asInt()) }
                worker("Очистка заголовка") { ticketValidating.subject = ticketValidating.subject.trim() }
                worker("Очистка описания") { ticketValidating.description = ticketValidating.description.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateTitleNotEmpty("Проверка на непустой заголовок")
                validateSubjectHasContent("Проверка на наличие содержания в заголовке")
                validateDescriptionNotEmpty("Проверка на непустое описание")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")

                finishTicketValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика сохранения"
                repoRead("Чтение заявки из БД")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление заявки в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Удалить объявление", TrackCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adValidating") {
                    ticketValidating = ticketRequest.deepCopy()
                }
                worker("Очистка id") { ticketValidating.id = TrackTicketId(ticketValidating.id.asInt()) }
                worker("Очистка lock") { ticketValidating.lock = TrackTicketLock(ticketValidating.lock.asInt()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateLockNotEmpty("Проверка на непустой lock")
                finishTicketValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика удаления"
                repoRead("Чтение заявки из БД")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление заявки из БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Поиск объявлений", TrackCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в ticketFilterValidating") { ticketFilterValidating = ticketFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishTicketFilterValidation("Успешное завершение процедуры валидации")
            }
            repoSearch("Поиск заявки в БД по фильтру")
            prepareResult("Подготовка ответа")
        }
    }.build()
}
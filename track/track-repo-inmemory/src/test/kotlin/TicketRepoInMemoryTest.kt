import ru.otus.otuskotlin.track.backend.repo.tests.*
import ru.otus.otuskotlin.track.repo.common.TicketRepoInitialized
import ru.otus.otuskotlin.track.repo.inmemory.TicketRepoInMemory

class TicketRepoInMemoryCreateTest : RepoTicketCreateTest() {
    override val repo = TicketRepoInitialized(
        TicketRepoInMemory(randomUuid = { uuidNew.asInt() }),
        initObjects = initObjects,
    )
}

class TicketRepoInMemoryDeleteTest : RepoTicketDeleteTest() {
    override val repo = TicketRepoInitialized(
        TicketRepoInMemory(),
        initObjects = initObjects,
    )
}

class TicketRepoInMemoryReadTest : RepoTicketReadTest() {
    override val repo = TicketRepoInitialized(
        TicketRepoInMemory(),
        initObjects = initObjects,
    )
}

class TicketRepoInMemorySearchTest : RepoTicketSearchTest() {
    override val repo = TicketRepoInitialized(
        TicketRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryUpdateTest : RepoTicketUpdateTest() {
    override val repo = TicketRepoInitialized(
        TicketRepoInMemory(),
        initObjects = initObjects,
    )
}